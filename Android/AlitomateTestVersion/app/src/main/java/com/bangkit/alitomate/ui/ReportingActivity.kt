package com.bangkit.alitomate.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bangkit.alitomate.R
import com.bangkit.alitomate.databinding.ActivityReportingBinding
import com.bangkit.alitomate.ml.Model
import com.bangkit.alitomate.model.Report
import com.bangkit.alitomate.utils.reduceFileImage
import com.bangkit.alitomate.utils.rotateBitmap
import com.bangkit.alitomate.utils.uriToFile
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit

class ReportingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportingBinding
    private lateinit var bitmap: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var storageRef = FirebaseStorage.getInstance().getReference()
    private var db = Firebase.firestore

    private var getFile: File? = null
    private var location: Location? = null
    private val imageSize = 224

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                Toast.makeText(this,
                    R.string.permission_alert,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        showLoading(false)

        setupAction()
        getMyLocation()
        createLocationRequest()
        createLocationCallback()
    }

    override fun onResume() {
        super.onResume()
        locationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setupAction() {
        binding.apply {
            cameraxButton.setOnClickListener { startCamera() }
            galleryButton.setOnClickListener { startGallery() }
            submitButton.setOnClickListener { startSubmit() }
        }
    }

    private fun startSubmit() {
        if (getFile != null) {
            showLoading(true)
            locationUpdates()
            val file = reduceFileImage(getFile as File)
            val name = binding.nameTextInputEdt.text.toString()
            val phoneNumber = binding.phoneNumTextInputEdt.text.toString()
            val detailLoc = binding.detailLocTextInputEdt.text.toString()
            val detailReport = binding.descTextInputEdt.text.toString()
            val imgValidation = binding.imgValidationTv.text
            val lat = location?.latitude.toString()
            val lon = location?.longitude.toString()

            when {
                name.isEmpty() -> {
                    binding.nameTextInputEdt.error = "Masukkan Nama Anda"
                    showLoading(false)
                }
                phoneNumber.isEmpty() -> {
                    binding.phoneNumTextInputEdt.error = "Masukkan Nomor Telepon Anda"
                    showLoading(false)
                }
                imgValidation != "Gambar Valid" -> {
                    Toast.makeText(this,R.string.img_report_error,Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
                else -> {
                    val imageRef = storageRef.child("images/${file.toUri().lastPathSegment}")
                    val uploadImage = imageRef.putFile(file.toUri())
                    uploadImage.addOnSuccessListener {
                        Log.i(TAG, "Image Uploaded $imageRef")
                        val downloadImageUrl = imageRef.downloadUrl
                        downloadImageUrl.addOnSuccessListener {
                            if (it != null) {
                                val report = Report(
                                    name,
                                    phoneNumber,
                                    detailLoc,
                                    detailReport,
                                    it.toString(),
                                    lat,
                                    lon,
                                    null)

                                db.collection("report")
                                    .add(report)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@ReportingActivity,"Upload Success", Toast.LENGTH_SHORT).show()
                                        showLoading(false)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@ReportingActivity,"Upload Gagal", Toast.LENGTH_SHORT).show()
                                        showLoading(false)
                                    }

                            } else {
                                Log.d(TAG, "imageUrl null")
                                showLoading(false)
                            }
                        }
                    }
                    uploadImage.addOnFailureListener {
                        Log.e(TAG, it.message ?: "No message")
                        showLoading(false)
                    }
                }
            }
        } else {
            Toast.makeText(this@ReportingActivity, R.string.form_error, Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun locationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)

    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type ="image/*"

        val chooser = Intent.createChooser(intent, "Pilih Gambar!")
        launcherIntentGallery.launch(chooser)
    }

    private fun predictImage(imagePredict: Bitmap) {
        try {
            val model = Model.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            inputFeature0.loadBuffer(byteBuffer)
            val intValues = IntArray(imageSize*imageSize)
            imagePredict.getPixels(intValues,0,imagePredict.width,0,0,imagePredict.width,imagePredict.height)
            var pixel = 0

            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val rgb = intValues[pixel++]
                    byteBuffer.putFloat((rgb shr 16 and 0xFF) * (1f/255))
                    byteBuffer.putFloat((rgb shr 8 and 0xFF) * (1f/255))
                    byteBuffer.putFloat((rgb and 0xFF) * (1f/255))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray

            //find index of the class with biggest confidence
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Gambar Valid", "Gambar invalid, tolong ulangi pemotretan agar api terdeteksi")
            binding.imgValidationTv.setText(classes[maxPos])

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            Toast.makeText(this,R.string.img_validate_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    location = loc
                } else {
                    Toast.makeText(
                        this,
                        R.string.loc_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this,
                        R.string.gps_alert,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImg.setImageBitmap(result)
            val imagePredict = Bitmap.createScaledBitmap(result, imageSize,imageSize,false)
            predictImage(imagePredict)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@ReportingActivity)

            getFile = myFile
            binding.previewImg.setImageURI(selectedImg)

            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImg)
            val resultPredict = Bitmap.createScaledBitmap(bitmap, imageSize,imageSize,false)
            predictImage(resultPredict)
        }
    }

    companion object {
        private const val TAG = "ReportingActivity"
        const val CAMERA_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}