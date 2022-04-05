package com.example.tinder.activity.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinder.activity.adapter.MatchedUserAdapter
import com.example.tinder.activity.model.CardItem
import com.example.tinder.databinding.ActivityMainBinding
import com.example.tinder.databinding.ActivityMatchedUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchedUserActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMatchedUserBinding.inflate(layoutInflater)}
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var usersDB: DatabaseReference
    private val adapter = MatchedUserAdapter()
    private val cardItems = mutableListOf<CardItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        usersDB = Firebase.database.reference.child("Users")
        initMatchedUserRecycelrView()
        getMatchUsers()

    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인 되어있지 않습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser!!.uid
    }

    private fun initMatchedUserRecycelrView() {
        binding.matchedUserRecyclerView.adapter = adapter
        binding.matchedUserRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getMatchUsers() {
        val matchedDB = usersDB.child(getCurrentUserID()).child("likedBy").child("match")
        matchedDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getUserByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {            }

            override fun onChildRemoved(snapshot: DataSnapshot) {            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {           }

            override fun onCancelled(error: DatabaseError) {            }

        })
    }

    private fun getUserByKey(userId: String) {
        usersDB.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItems.add(CardItem(userId, snapshot.child("name").value.toString()))
                adapter.submitList(cardItems)
            }

            override fun onCancelled(error: DatabaseError) { }

        })
    }
}