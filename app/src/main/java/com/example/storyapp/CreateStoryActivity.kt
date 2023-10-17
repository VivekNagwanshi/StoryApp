package com.example.storyapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.storyapp.databinding.ActivityCreateStoryBinding
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private val storyImageUris: ArrayList<StoryModel> = ArrayList()
    private lateinit var storyPreference: StoryData
    var headLine = ""
    var opinion = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_story)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_story)
        initUI()
    }
    private fun initUI(){
        val byteArray = intent.getByteArrayExtra("bitmap")
        storyPreference = StoryData(this)
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.newsImage.setImageBitmap(bitmap);
        } else {
            // Handle the case where no Bitmap was passed
        }

        binding.btnSaveStory.setOnClickListener {
            val byteArray = intent.getByteArrayExtra("bitmap")
            if (binding.headlineEditText.text.toString().isNotEmpty()){
                headLine = binding.headlineEditText.text.toString()
            }
            if (binding.opinionEditText.text.toString().isNotEmpty()){
                opinion = binding.opinionEditText.text.toString()
            }
            if (byteArray != null) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                binding.newsImage.setImageBitmap(bitmap);
                val bitmapToUri = bitmapToUri(this,bitmap)
                storyImageUris.add(StoryModel(
                    bitmapToUri.toString(),
                    headLine,
                    opinion
                ))
                storyPreference.saveStoryArraylist(storyImageUris)
                val dd = storyPreference.storyArraylist
                Log.e("story",Gson().toJson(dd))
                finishActivity(101);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                // Handle the case where no Bitmap was passed
            }

        }
    }
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        try {
            // Create a file in the cache directory with a unique name
            val file = File(context.cacheDir, "image.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            // Get the content URI for the file
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            return uri
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}