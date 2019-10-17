package com.mobbile.paul.mttms.ui.outlets.mapoutlet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.mobbile.paul.mttms.BuildConfig
import kotlinx.android.synthetic.main.activity_attach_photo.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.mobbile.paul.mttms.ui.customers.Customers
import com.mobbile.paul.mttms.util.Util.showMsgDialog
import com.mobbile.paul.mttms.util.Util.showSomeDialog


class AttachPhotos : BaseActivity(), View.OnClickListener {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: MapOutletViewModel

    private lateinit var upload: Button

    private lateinit var save_pics: ImageView

    private var fileUri: Uri? = null

    private var mImageFileLocation = ""

    private var imageView: ImageView? = null

    private var postPath: String? = null


    var custName: String? = null
    var contactName: String? = null
    var address: String? = null
    var phones: String? = null
    var outletClass: Int? = null
    var prefLang: Int? = null
    var outletTypeId: Int? = null
    var repid: Int? = null
    var lat: String? = null
    var lng: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attach_photo)
        vmodel = ViewModelProviders.of(this, modelFactory)[MapOutletViewModel::class.java]
        upload = findViewById(R.id.upload)
        save_pics = findViewById(R.id.save_pics)
        imageView = findViewById(R.id.imageView)

        showProgressBar(false)

        upload.setOnClickListener(this)
        save_pics.setOnClickListener(this)

        back_btn.setOnClickListener {
            onBackPressed()
        }

        custName = intent.getStringExtra("outletName")!!
        contactName = intent.getStringExtra("contactName")!!
        address = intent.getStringExtra("address")!!
        phones = intent.getStringExtra("phones")!!
        outletClass = intent.getIntExtra("outletClass",0)
        prefLang = intent.getIntExtra("prefLang",0)
        outletTypeId = intent.getIntExtra("outletTypeId",0)
        repid = intent.getIntExtra("repid",0)
        lat = intent.getStringExtra("lat")!!
        lng = intent.getStringExtra("lng")!!

    }

    @SuppressLint("SimpleDateFormat")
    fun checkCameraPermission(values: Int) {
        val accessPermissionStatus =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (accessPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            requestImagePermission()
        } else {
            when (values) {
                1 -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(pickPhoto, REQUEST_PICK_PHOTO)
                }
                2 -> {
                    val callCameraApplicationIntent = Intent()
                    callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
                    val photoFile: File?
                    try {
                        photoFile = createImageFile()
                        val outputUri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile
                        )
                        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                        callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                3 -> {
                    if (!postPath.isNullOrBlank()) {
                        showProgressBar(true)
                        save_pics.visibility = View.INVISIBLE
                        vmodel.createNewCards(
                            repid!!, outletClass!!, prefLang!!, outletTypeId!!, custName!!, address!!, contactName!!, phones!!, lat!!, lng!!,
                            postPath!!, SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date()), SimpleDateFormat("yyyy-MM-dd").format(Date())
                        ).observe(this, ObserveImage)
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    internal fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "MT_" + timeStamp
        var storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Images")
        if (!storageDirectory!!.exists()) storageDirectory.mkdir()
        val image = File(storageDirectory, imageFileName + ".jpg")
        mImageFileLocation = image.absolutePath
        return image
    }

    private fun requestImagePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_PICK_PHOTO
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PICK_PHOTO -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestImagePermission()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.upload -> {
                checkCameraPermission(2)
            }
            R.id.save_pics -> {
                checkCameraPermission(3)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("file_uri", fileUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        fileUri = savedInstanceState.getParcelable("file_uri")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    val dp = getPathDeprecated(this, data.data)
                    imageView!!.setImageURI(data.data)
                    Log.d(TAG, dp!!)
                    postPath = dp
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                Glide.with(this)
                    .load(mImageFileLocation)
                    .centerCrop()
                    .into(imageView!!)
                postPath = mImageFileLocation
            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getPathDeprecated(ctx: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = ctx.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnindex)
        }
        return uri.path
    }

    private val ObserveImage = Observer<String> {
        if(it=="OK") {
            showMsgDialog(Customers(), this, "Successful", "Customer Created successfully")
        }else{
            showSomeDialog(this, "Fail to add new customer, please contact MT monitor", "Outlet Error")
        }
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111
        val TAG = "TakeOutletPicture"
    }
}

