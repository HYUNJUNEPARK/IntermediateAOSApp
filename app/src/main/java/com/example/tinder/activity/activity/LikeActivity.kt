package com.example.tinder.activity.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder.R
import com.example.tinder.activity.adapter.CardItemAdapter
import com.example.tinder.activity.key.DBKey.Companion.DISLIKE
import com.example.tinder.activity.key.DBKey.Companion.LIKE
import com.example.tinder.activity.key.DBKey.Companion.LIKED_BY
import com.example.tinder.activity.key.DBKey.Companion.MATCH
import com.example.tinder.activity.key.DBKey.Companion.NAME
import com.example.tinder.activity.key.DBKey.Companion.USER_ID
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
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(NAME).value == null) {
                    showNameInputPopup()
                    return
                }
                getUnSelectedUsers()
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        currentUserDB.addListenerForSingleValueEvent(listener)
    }

    private fun getUnSelectedUsers() {
        val listener = object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.child(USER_ID).value != getCurrentUserID() //내가 아닌 사람
                    && snapshot.child(LIKED_BY).child(LIKE).hasChild(getCurrentUserID()).not() //나한테 like/dislike 받지 않음 사람
                    && snapshot.child(LIKED_BY).child(DISLIKE).hasChild(getCurrentUserID()).not() ) {

                    val userId = snapshot.child(USER_ID).value.toString()
                    var name = "undecided" //이름을 설정하지 않았을 때
                    if (snapshot.child(NAME).value != null) {
                        name = snapshot.child(NAME).value.toString()
                    }
                    cardItems.add(CardItem(userId, name))
                    adapter.submitList(cardItems)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                cardItems.find { CardItem ->
                    CardItem.userId == snapshot.key
                }?.let { CardItem ->
                    CardItem.name = snapshot.child(NAME).value.toString()
                }
                adapter.submitList(cardItems)
                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {  }
            override fun onCancelled(error: DatabaseError) { }
        }
        usersDB.addChildEventListener(listener)
    }

    private fun showNameInputPopup() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("이름을 입력해주세요")
            .setView(editText)
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
        user[USER_ID] = userId
        user[NAME] = name
        currentUserDB.updateChildren(user)
        getUnSelectedUsers()
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, getString(R.string.toast_not_login), Toast.LENGTH_SHORT).show()
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
            else -> { }
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
            .child(LIKED_BY)
            .child(LIKE)
            .child(getCurrentUserID())
            .setValue(true)
        saveMatchIfOtherUserLikedMe(card.userId)
        Toast.makeText(this, "Like! Matched!", Toast.LENGTH_SHORT).show()
    }

    private fun dislike() {
        val card = cardItems[manager.topPosition - 1] //idx 는 0부터 시작
        cardItems.removeFirst()

        usersDB.child(card.userId)
            .child(LIKED_BY)
            .child(DISLIKE)
            .child(getCurrentUserID())
            .setValue(true)
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB = usersDB.child(getCurrentUserID()).child(LIKED_BY).child(LIKE).child(otherUserId)
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    usersDB.child(getCurrentUserID())
                        .child(LIKED_BY)
                        .child(MATCH)
                        .child(otherUserId)
                        .setValue(true)

                    usersDB.child(getCurrentUserID())
                        .child(LIKED_BY)
                        .child(MATCH)
                        .child(getCurrentUserID())
                        .setValue(true)
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        otherUserDB.addListenerForSingleValueEvent(listener)
    }
}
    

