package com.example.tinder.activity.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinder.R
import com.example.tinder.activity.key.DBKey.Companion.LIKED_BY
import com.example.tinder.activity.key.DBKey.Companion.MATCH
import com.example.tinder.activity.key.DBKey.Companion.NAME
import com.example.tinder.activity.key.DBKey.Companion.USERS
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

    private fun initMatchedUserRecycelrView() {
        binding.matchedUserRecyclerView.adapter = adapter
        binding.matchedUserRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getMatchUsers() {
        val matchedDB = usersDB.child(getCurrentUserID()).child(LIKED_BY).child(MATCH)
        val listener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userId = snapshot.key
                if (userId?.isNotEmpty() == true) {
                    getUserByKey(userId.orEmpty())
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }
        }
        matchedDB.addChildEventListener(listener)
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, getString(R.string.toast_not_login), Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser!!.uid
    }

    private fun getUserByKey(userId: String) {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(NAME).value.toString()
                cardItems.add(CardItem(userId, name))
                adapter.submitList(cardItems)
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        usersDB.child(userId).addListenerForSingleValueEvent(listener)
    }
}