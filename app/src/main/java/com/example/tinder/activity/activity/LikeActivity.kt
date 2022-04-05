package com.example.tinder.activity.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder.activity.CardItemAdapter
import com.example.tinder.activity.activity.LoginActivity.Companion.TAG
import com.example.tinder.activity.model.CardItem
import com.example.tinder.databinding.ActivityLikeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class LikeActivity : AppCompatActivity(), CardStackListener {
    private val binding by lazy { ActivityLikeBinding.inflate(layoutInflater) }
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var usersDB: DatabaseReference
    private val adapter = CardItemAdapter()
    private val cardItems = mutableListOf<CardItem>()
    private val manager by lazy {
        CardStackLayoutManager(this, this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDB()
        initCardStackView()
        initMatchedListButton()
        initSignOutButton()
    }
    private fun initMatchedListButton() {
        binding.matchListButton.setOnClickListener {
            startActivity(Intent(this, MatchedUserActivity::class.java))
        }
    }

    private fun initSignOutButton() {
        binding.SignOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initDB() {
        usersDB = Firebase.database.reference.child("Users")
        val currentUserDB = usersDB.child(getCurrentUserID())
        //DB 에서 데이터를 받아올때는 대부분 리스너를 사용
        val listener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //스냅샷으로 데이터가 넘어옴
                //이름이 변경되었을 때 다른 사람이 나를 좋아요했을 때 데이터가 바뀜
                if (snapshot.child("name").value == null) {
                    showNameInputPopup()
                    return
                }
                //유저 정보 갱신
                getUnSelectedUsers()

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: $error")
            }
        }
        currentUserDB.addListenerForSingleValueEvent(listener)
    }

    private fun getUnSelectedUsers() {
        //DB 모든 변경 사항들이 아래 리스너에 걸림
        usersDB.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //내가 한번도 선택한적이 없는 유저의 조
                if( snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("dislike").hasChild(getCurrentUserID()).not() ) {

                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided" //이름을 설정하지 않았을 때
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }
                    cardItems.add(CardItem(userId, name))
                    adapter.submitList(cardItems)
                    adapter.notifyDataSetChanged()
                }

            }
            //이름 변경이나 like 등
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //일치하는 이름을 찾아서 수정
                cardItems.find { CardItem ->
                    CardItem.userId == snapshot.key
                }?.let { CardItem ->
                    CardItem.name = snapshot.child("name").value.toString()
                }
                adapter.submitList(cardItems)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun showNameInputPopup() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("이름을 입력해주세요")
            .setView(editText) // editText 뷰를 추가할 수 있음
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                }
                else {
                    saveUserName(editText.text.toString())
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUserName(name: String) {
        val userId = getCurrentUserID()
        val currentUserDB = usersDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        user["name"] = name
        currentUserDB.updateChildren(user)

        //todo 유저 정보 가져와라
        getUnSelectedUsers()
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인 되어있지 않습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser!!.uid
    }

//CardStackView
    private fun initCardStackView() {
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
    }
    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> like()
            Direction.Left -> dislike()
            else -> {

            }
        }
    }
    override fun onCardDragging(direction: Direction?, ratio: Float) {    }
    override fun onCardRewound() {    }
    override fun onCardCanceled() {    }
    override fun onCardAppeared(view: View?, position: Int) {    }
    override fun onCardDisappeared(view: View?, position: Int) {    }

    private fun like() {
        val card = cardItems[manager.topPosition - 1] //idx 는 0부터 시작
        cardItems.removeFirst()

        usersDB.child(card.userId)
            .child("likedBy")
            .child("like")
            .child(getCurrentUserID())
            .setValue(true)

        saveMatchIfOtherUserLikedMe(card.userId)

        // todo 매칭 처리
        Toast.makeText(this, "Like! Matched!", Toast.LENGTH_SHORT).show()
    }

    private fun dislike() {
        val card = cardItems[manager.topPosition - 1] //idx 는 0부터 시작
        cardItems.removeFirst()

        usersDB.child(card.userId)
            .child("likedBy")
            .child("disLike")
            .child(getCurrentUserID())
            .setValue(true)
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB = usersDB.child(getCurrentUserID()).child("likedBy").child("like").child(otherUserId)
        otherUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    usersDB.child(getCurrentUserID())
                        .child("likedBy")
                        .child("match")
                        .child(otherUserId)
                        .setValue(true)

                    usersDB.child(getCurrentUserID())
                        .child("likedBy")
                        .child("match")
                        .child(getCurrentUserID())
                        .setValue(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}
    

