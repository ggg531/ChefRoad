package com.example.chefroad.feature.review


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.chefroad.feature.model.Channel
import com.example.chefroad.feature.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ReviewViewModel @Inject constructor(): ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    var reviews = _reviews.asStateFlow()

    private val _state = MutableStateFlow<SignOutState>(SignOutState.Nothing)
    val state = _state.asStateFlow()

    init {
        getReview()
    }

    fun getReview() {
        firebaseDatabase.getReference("review").get()
            .addOnSuccessListener {
                val list = mutableListOf<Review>()
                it.children.forEach { data ->
                    val id = data.child("id").value.toString()
                    //val id = data.key ?: ""
                    val restaurantName = data.child("restaurantName").value.toString() // 적절한 데이터 경로 설정
                    val content = data.child("content").value.toString()
                    val createAt = data.child("createAt").value as? Long ?: System.currentTimeMillis()

                    val review = Review(
                        id = id,
                        restaurantName = restaurantName,
                        content = content,
                        createAt = createAt
                    )
                    list.add(review)

                }
                _reviews.value = list
            }
    }

    fun addReview(restaurantName: String, content: String) {
        val key = firebaseDatabase.getReference("review").push().key

        // Review 객체 생성
        val review = Review(
            id = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            restaurantName = restaurantName,
            content = content,
            createAt = System.currentTimeMillis()
        )

        firebaseDatabase.getReference("review").child(key!!).setValue(review)
            .addOnSuccessListener {
                getReview()
            }
    }

    fun modifyReview(reviewId: String, newContent: String) {
        val db = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            db.collection("reviews").document(reviewId)
                .update("content", newContent)  // 리뷰 내용 수정
                .addOnSuccessListener {
                    // 수정 성공 시 리뷰 리스트 갱신
                    _reviews.value = _reviews.value.map {
                        if (it.id == reviewId) {
                            it.copy(content = newContent) // 내용 수정
                        } else {
                            it
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // 수정 실패 시 처리
                }
        }
    }

    fun deleteReview(reviewId: String) {
        val db = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            db.collection("reviews").document(reviewId)
                .delete()
                .addOnSuccessListener {
                    // 삭제 성공

                    _reviews.value = _reviews.value.filter { it.id != reviewId }
                }
                .addOnFailureListener { exception ->
                    // 삭제 실패

                }
        }
    }



    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _state.value = SignOutState.LoggedOut
    }

}

sealed class SignOutState {
    object Nothing: SignOutState()
    object LoggedOut: SignOutState()
}

