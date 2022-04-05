package com.example.tinder.activity.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinder.R
import com.example.tinder.activity.DBKey.Companion.LIKED_BY
import com.example.tinder.activity.DBKey.Companion.MATCH
import com.example.tinder.activity.DBKey.Companion.NAME
import com.example.tinder.activity.DBKey.Companion.USERS
import com.example.tinder.activity.adapter.MatchedUserAdapter
import com.example.tinder.activity.model.CardItem
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

        usersDB = Firebase.database.reference.child(USERS)
        initMatchedUserRecycelrView()
        getMatchUsers()

    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, getString(R.string.toast_not_login), Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser!!.uid
    }

    private fun initMatchedUserRecycelrView() {
        binding.matchedUserRecyclerView.adapter = adapter
        binding.matchedUserRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getMatchUsers() {
        val matchedDB = usersDB.child(getCurrentUserID()).child(LIKED_BY).child(MATCH)
        matchedDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getUserByKey(snapshot.key.orEmpty())
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun getUserByKey(userId: String) {
        usersDB.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItems.add(CardItem(userId, snapshot.child(NAME).value.toString()))
                adapter.submitList(cardItems)
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }
}