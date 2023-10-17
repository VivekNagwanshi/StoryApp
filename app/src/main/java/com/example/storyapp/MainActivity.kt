package com.example.storyapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.storyapp.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var storyImageUris: ArrayList<StoryModel> = ArrayList()
    private lateinit var storyPreference: StoryData
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private val PICK_IMAGE_REQUEST = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        val storyView = findViewById<StoryView>(R.id.storyView)
        storyView.setActivityContext(this)
        storyView.resetStoryVisits()
        binding.ivProfile.setBorderColor(Color.parseColor("#FFA500"))
        storyPreference = StoryData(this)
        val uris = java.util.ArrayList<StoryModel>()
        storyImageUris.add(
            StoryModel(
                "https://i.pinimg.com/564x/6e/c6/36/6ec63696a47e2316a0b7150b9e564765.jpg",
                "",
                "ISRO to lunch surveillance satellite"
            )
        )
//        storyView?.setImageUris(uris)
        binding.ivProfile.setOnClickListener {
            navigateToStoryPlayerPage()
        }
        binding.toolbar.ivBackBtn.setImageDrawable(getDrawable(R.drawable.ic_arrow_back))
        binding.toolbar.ivBackBtn.setOnClickListener {
            finish()
        }
        /*binding.ivAddStory.setOnClickListener {
            if (checkAndRequestPermissions(this)) {
                chooseImage(this)
            }
        }*/
        /*for (i in storyPreference.storyArraylist) {
            storyImageUris.add(i)
        }*/
        val ss = storyPreference.storyArraylist
//        storyPreference.saveStoryArraylist(storyImageUris)
        if (!ss.isNullOrEmpty()) {
            Log.e("StoryList", ss[0].name)
        } else {
            Log.e("Empty", "empty list")
        }


    }

    private fun navigateToStoryPlayerPage() {
        val intent = Intent(this, StoryPlayer::class.java)
        intent.putParcelableArrayListExtra(StoryPlayer.STORY_IMAGE_KEY, storyImageUris)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                chooseImage(this@MainActivity)
            }
            101 -> {
                val storyPreference = storyPreference.storyArraylist
                Log.e("Vivek", storyPreference.get(0).name)
            }
        }
    }

    private fun checkAndRequestPermissions(context: Activity?): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Limit to image files
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // function to let's the user to choose image from camera or gallery
    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Remove profile"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                openGallery()
            } else if (optionsMenu[i] == "Remove profile") {
                binding.ivProfile.setImageDrawable(null);
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val photo = data.extras!!["data"] as Bitmap
                    val bitmap: Bitmap = photo
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    val intent = Intent(this, CreateStoryActivity::class.java)
                    intent.putExtra("bitmap", byteArray)
                    startActivityForResult(intent, 101)
//                    binding.ivProfile.setImageBitmap(photo)
                }
                10 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImageUri = data.data
                    binding.ivProfile.setImageURI(selectedImageUri)
                }
            }
        }
    }
}