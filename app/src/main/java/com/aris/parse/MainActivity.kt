package com.aris.parse


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aris.parse.databinding.ActivityMainBinding
import com.aris.parse.model.Daneshjoo
import com.parse.GetCallback
import com.parse.GetDataCallback
import com.parse.ParseFile
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.SaveCallback


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = ParseUser.getCurrentUser()
        val file = user.getParseFile("Image")
        file!!.getDataInBackground(GetDataCallback { data, e ->
            if (e == null) {
                val bitmapPic = BitmapFactory.decodeByteArray(data, 0, data.size)
                binding.profileImage1.setImageBitmap(bitmapPic)
            }
        })


        binding.button.setOnClickListener {
           ParseUser.logOut()
            startActivity(Intent(this,InSign::class.java))
            finish()
        }

    }

    private fun sendData() {
        val obj = ParseObject("person")
        obj.put("name", "Arman Aris")

        obj.saveInBackground { error ->
            if (error == null) {
                Toast.makeText(this, "اطلاعات ثبت شد", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "مشکل در فرستادن اطلاعات", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendDataAndCreateClassInAndroid() {

        val obj1 = ParseObject("game")
        obj1.put("name", "Last of Us")
        obj1.put("type", "ps5")
        obj1.put("price", 70)
        obj1.put("Play", true)

        val obj2 = ParseObject("game")
        obj2.put("name", "God of War 4")
        obj2.put("type", "ps4")
        obj2.put("price", 60)
        obj2.put("Play", true)

        val obj3 = ParseObject("game")
        obj3.put("name", "Forza Horizon 5")
        obj3.put("type", "Xbox")
        obj3.put("price", 70)
        obj3.put("Play", false)

        obj1.save()
        obj1.saveInBackground()
        obj2.saveInBackground { error ->
            if (error == null) {
                Toast.makeText(this, "اطلاعات ثبت شد", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "مشکل در فرستادن اطلاعات", Toast.LENGTH_SHORT).show()
            }
        }

        obj3.saveInBackground()

        // no network
        obj3.saveEventually()
        obj3.saveEventually { }

        obj1.delete()
        obj1.deleteInBackground()
        obj1.deleteInBackground {  }

        obj1.deleteEventually()
        obj1.deleteEventually{ }

    }

    private fun sendListData(daneshjooha: List<Daneshjoo>) {
        daneshjooha.forEach { daneshjoo ->
            val student = ParseObject("Daneshjoo")
            student.put("uid", daneshjoo.id!!)
            student.put("Name", daneshjoo.name)
            student.put("Number", daneshjoo.number)
            student.put("University", daneshjoo.university)
            student.put("StudentId", daneshjoo.studentId)

            student.saveInBackground()
        }
    }

    private fun getData() {
        val allData = ParseQuery<ParseObject>("game")

        allData.getInBackground("D7up4fUmGD")

        allData.findInBackground { all_objects, e ->
            if (e == null) {
                all_objects.forEach {
                    Log.d("7171", it.toString()) //show id

                    Log.d("7171", it.getString("name").toString())

                    val get = it.get("name") // type any

                    val get1 = it.get("name") as String
                }
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        allData.whereEqualTo("type", "ps4").findInBackground { all_objects, e ->
            if (e == null) {
                all_objects.forEach {
                    Log.d("7171", it.getString("name").toString())
                }
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun getDaneshgooFromServerByStudentId(id: Int): Daneshjoo {

        // tmp value =>
        var daneshjoo = Daneshjoo(100000000, " ", " ", " ", -1)

        val query = ParseQuery<ParseObject>("Daneshjoo")

        query.whereEqualTo("uid", id).getFirstInBackground { obj, error ->

            if (error == null) {

                daneshjoo = Daneshjoo(
                    name = obj.getString("Name").toString(),
                    number = obj.getString("Number").toString(),
                    university = obj.getString("University").toString(),
                    studentId = obj.getInt("StudentId")
                )

            } else {
                Toast.makeText(this, "error : ${error.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
        return daneshjoo
    }

    fun getAllDaneshgooFromServer(): List<Daneshjoo> {

        val daneshjooList = ArrayList<Daneshjoo>()

        val query = ParseQuery<ParseObject>("Daneshjoo")

        val listFromServer = query.find()

        listFromServer.forEach { obj_from_server ->

            daneshjooList.add(
                Daneshjoo(
                    name = obj_from_server.getString("Name").toString(),
                    number = obj_from_server.getString("Number").toString(),
                    university = obj_from_server.getString("University").toString(),
                    studentId = obj_from_server.getInt("StudentId")
                )
            )

        }

        return daneshjooList

    }

    private fun sendDataAndCreateClassInAndroidToLocalDB() {
        val obj1 = ParseObject("game")
        obj1.put("name", "Last of Us")
        obj1.put("type", "ps5")
        obj1.put("price", 70)
        obj1.put("Play", true)

        val obj2 = ParseObject("game")
        obj2.put("name", "God of War 4")
        obj2.put("type", "ps4")
        obj2.put("price", 60)
        obj2.put("Play", true)


        obj1.pin()
        obj1.pinInBackground()

        obj2.pinInBackground { error ->
            if (error == null) {
                Toast.makeText(this, "اطلاعات ثبت شد", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "مشکل در فرستادن اطلاعات", Toast.LENGTH_SHORT).show()
            }

        }

        obj1.unpin()
        obj1.unpinInBackground()

        obj2.unpinInBackground { }

    }

    private fun getLocalData() {
        val allData = ParseQuery<ParseObject>("game")
        allData.fromLocalDatastore()

        allData.getInBackground("D7up4fUmGD")

        allData.findInBackground { all_objects, e ->
            if (e == null) {
                all_objects.forEach {
                    Log.d("7171", it.toString()) //show id

                    Log.d("7171", it.getString("name").toString())

                    val get = it.get("name") // type any

                    val get1 = it.get("name") as String
                }
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        allData.whereEqualTo("type", "ps4").findInBackground { all_objects, e ->
            if (e == null) {
                all_objects.forEach {
                    Log.d("7171", it.getString("name").toString())
                }
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun workMoreWithParseQuery() {

        // soccer - player
        val query = ParseQuery<ParseObject>("player")

        // receive an object using objectId
        query.get("objectId")
        query.getInBackground("objectId", GetCallback { `object`, e -> })

        // receive a list of all objects =>
        query.findInBackground { objects, e -> }

        // not in background thread =>
        val list = query.find()
        list.forEach { }

        // set limit for receiving data =>
        query.setLimit(12)

        // filter equal
        query.whereEqualTo("age", 27)

        // filter not equal
        query.whereNotEqualTo("age", 27)

        // age > 15
        query.whereGreaterThan("age", 15)

        // age < 15
        query.whereLessThan("age", 15)

        //age >= 15
        query.whereGreaterThanOrEqualTo("age", 15)

        //age =< 15
        query.whereLessThanOrEqualTo("age", 15)

        // when we want only one data - background thread
        query.getFirstInBackground { `object`, e -> }

        // get first in ui thread
        val theFirstOne = query.first

        // when we want to skip first 20 data =>
        query.setSkip(20)

        // sorting =>
        query.orderByAscending("age")
        query.orderByDescending("age")

        // second level sort =>
        query.addAscendingOrder("played_game")
        query.addDescendingOrder("played_game")

        // two level sort
        query.orderByAscending("age").addAscendingOrder("played_game")
        query.orderByDescending("age").addDescendingOrder("played_game")

        // find specific players =>
        val listOfPlayers1 = listOf("amir", "hasan", "sajad")
        query.whereContainedIn("name", listOfPlayers1)

        // negative of top =>
        val listOfPlayers2 = listOf("amirAli", "mohammad", "mehdi")
        query.whereNotContainedIn("name", listOfPlayers2)

        // receive players that have familyName =>
        query.whereExists("familyName")

        // negative of top =>
        query.whereDoesNotExist("familyName")

        // in Strings =>
        query.whereStartsWith("name", "Amir")
        // exm = AmirAli - AmirHossein - AmirMohammadi ...

        // count data =>
        val countOfDataInServer = query.count()
        val countOfDataInServer1 = query.countInBackground { count, e -> }

    }

    private fun changeData() {
        val user = ParseUser.getCurrentUser()

        user.username = "user name"
        user.setPassword("password")
        user.email = "email address"
        user.put("column name", "value")


    }

    private fun uploadTextFile() {
        val context = "this is a good player"
        val nameFile = "context.txt"

        val byteArray = context.toByteArray()  // string to byteArray
        val string = String(byteArray)         // byteArray to string

        // create file
        val file = ParseFile(nameFile, byteArray)

        //save to server
        // file.saveInBackground({ it==error },{ Progress==position upload })
        file.saveInBackground(SaveCallback {
            if (it == null) {
                //upload file
                val obj = ParseObject("File")
                obj.put("Text", file)

                obj.saveInBackground { ex ->
                    if (ex == null) {
                        Toast.makeText(this, "File Upload", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun downloadTextFile() {

        val query = ParseQuery<ParseObject>("File")

        query.getFirstInBackground { obj, e ->
            if (e == null) {
                val downloadFile = obj.getParseFile("Text")
                //get file
                downloadFile!!.getDataInBackground(GetDataCallback { data, ex ->
                    if (ex == null) {
                        val string = String(data)
                        Toast.makeText(this, string.toString(), Toast.LENGTH_SHORT).show()
                    }
                })

            }
        }
    }

}