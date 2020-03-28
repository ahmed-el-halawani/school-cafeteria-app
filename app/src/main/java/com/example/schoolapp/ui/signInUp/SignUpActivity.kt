package com.example.schoolapp.ui.signInUp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.utils.Utils
import com.example.schoolapp.model.Stuffs
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.signup_form.*
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private var image:Uri? = null
    private lateinit var imageLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_form)
        imageLink = ""
    }

    override fun onStart() {
        super.onStart()

        iv_profileRegister.setOnClickListener {
            getImage()
        }

        btn_signUpRegister.setOnClickListener {


            signUpRegisterButton()
        }

    }


    private fun signUpRegisterButton(){
        val userName = et_parentNameRegister.text.toString()
        val email = et_parentEmailRegister.text.toString()
        val password = et_parentpasswordRegister.text.toString()
        val confirmPassword = et_parentpasswordRegister2.text.toString()

        when(emailPasswordChickValidate(userName,email,password,confirmPassword)){
            "USER_NAME_EMPTY"->et_parentNameRegister.error = "user name must be not empty"
            "EMAIL_AND_PASSWORD_EMPTY"->{
                et_parentEmailRegister.error = "must be not empty"
                et_parentpasswordRegister.error = "must be not empty"
            }
            "EMAIL_EMPTY"->et_parentEmailRegister.error = "email must be not empty"
            "EMAIL_NOT_VALID"->et_parentEmailRegister.error = "email not valid"
            "PASSWORD_EMPTY"->et_parentpasswordRegister.error = "password must be not empty"
            "PASSWORD_MISMATCH"->et_parentpasswordRegister2.error = "not matching password"
            else->{
                lo_progressBar.visibility = View.VISIBLE
                emailPasswordFireBaseChick(email,password,userName)
            }

        }
    }

    private fun emailPasswordChickValidate(userName: String,email:String, password: String,confirmPassword:String):String{
        val patternEmail = Regex("""\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,6}""")
        val emailChick = patternEmail.matches(email)
        // EMPTY CHICK
        if (userName.isEmpty()){
            return "USER_NAME_EMPTY"
        }

        if (email.isEmpty() && password.isEmpty()){
            return "EMAIL_AND_PASSWORD_EMPTY"
        }

        if (email.isEmpty()){
            return "EMAIL_EMPTY"
        }

        if(!emailChick){
            return "EMAIL_NOT_VALID"
        }

        if (password.isEmpty()){
            return "PASSWORD_EMPTY"
        }

        if (password != confirmPassword){
            return "PASSWORD_MISMATCH"
        }

        return ""
    }

    private fun emailPasswordFireBaseChick(email:String, password: String, userName: String){
        Fb.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if (it.isSuccessful){
                lo_progressBar.visibility = View.GONE
                Fb.auth.signOut()
                et_parentEmailRegister.error = "email already exist"
            } else if (Utils.isNotConnect(this)) {
                Toast.makeText(this,"no internet connection",Toast.LENGTH_LONG).show()
            }else{
                val x = it.exception as FirebaseAuthException
                when(x.errorCode){
                    Keys.USER_NOT_FOUND-> createUser(email,password,userName)
                    else->{
                        lo_progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext,x.errorCode,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun createUser(email:String, password:String, userName:String){
        Fb.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { signIn ->
            if(signIn.isSuccessful){
                upLoadImage()
                val school = Stuffs(email,password,userName,imageLink)
                FirebaseDatabase.getInstance().getReference(Keys.SchoolWorker)
                    .child(Fb.auth.uid!!)
                    .setValue(school)
                    .addOnCompleteListener {
                        if(signIn.isSuccessful){
                            Toast.makeText(baseContext, "DONE",Toast.LENGTH_SHORT).show()
                            Fb.auth.signOut()
                            startActivity(Intent(this,SignInActivity::class.java))
                            finishAffinity()
                        }
                    }
            }
        }
    }


    private fun getImage(){
        val selectImage = Intent()
        selectImage.type = "image/*"
        selectImage.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(selectImage,2)
    }

    private fun upLoadImage(){
        if(image != null){
            val imageName = "schoolWorkers/" + UUID.randomUUID().toString()
            val upLoadImage = Fb.storage.child(imageName)
            upLoadImage.putFile(image!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    imageLink = imageName
                }
            }
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data != null){
            image = data.data
            Picasso.get().load(image).into(iv_profileRegister)
        }
    }
}



