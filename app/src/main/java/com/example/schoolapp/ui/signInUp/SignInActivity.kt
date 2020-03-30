package com.example.schoolapp.ui.signInUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.schoolapp.MainActivity
import com.example.schoolapp.R
import com.example.schoolapp.control.utils.DBEvent
import com.example.schoolapp.control.utils.Fb
import com.example.schoolapp.control.utils.Keys
import com.example.schoolapp.control.utils.Utils
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.signin_form.*

class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_form)

    }

    override fun onStart() {
        super.onStart()


        btn_signIn2.setOnClickListener {
            signInChickButton()
        }

        btn_signUp2.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

//        DBEvent(this){
//            for (i in it.child(Keys.SchoolWorker).children){
//                Fb.db.child(Keys.SchoolWorker).child(i.key!!).child("id").setValue(i.key)
//            }
//        }.setSingleEvent()

    }

    private fun signInChickButton(){
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        when(emailPasswordChickValidate(email,password)){
            "EMAIL_AND_PASSWORD_EMPTY"->{
                et_email.error = "must be not empty"
                et_password.error = "must be not empty"
            }
            "EMAIL_EMPTY"->et_email.error = "email must be not empty"
            "EMAIL_NOT_VALID"->et_email.error = "email not valid"
            "PASSWORD_EMPTY"->et_password.error = "password must be not empty"
            "PASSWORD_MISMATCH"->et_password.error = "not matching password"
            else->{
                lo_progressBar.visibility = View.VISIBLE
                signInUser(email,password)
            }

        }
    }

    private fun emailPasswordChickValidate(email:String, password: String):String{
        val patternEmail = Regex("""[a-zA-Z_.\w]+@[a-zA-Z_]+?\.[a-zA-Z]{2,6}""")
        val emailChick = patternEmail.matches(email)

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

        return ""
    }

    private fun signInUser(email: String,password: String){
        Fb.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finishAffinity()
                } else if (Utils.isNotConnect(this)) {
                    Toast.makeText(this,"no internet connection",Toast.LENGTH_LONG).show()
                    lo_progressBar.visibility = View.GONE

                }else{
                    lo_progressBar.visibility = View.GONE
                    val x = it.exception as FirebaseAuthException
                    when(x.errorCode){
                        Keys.USER_NOT_FOUND-> {
                            et_email.error = "email not found"
                            Toast.makeText(applicationContext,"email or password is incorrect",Toast.LENGTH_LONG).show()
                        }
                        else->Toast.makeText(applicationContext,x.errorCode,Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    override fun onBackPressed() {
        finishAffinity()
        Fb.auth.signOut()
    }
}
