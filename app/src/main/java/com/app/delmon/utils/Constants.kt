package com.app.delmon.utils

import android.Manifest



object Constants {

    object User{
        var  apptype = 1
        var  usertype = "USER"
        var  mobileno = ""
        var  Wallet = ""
        var  loyalty = ""
        var  otp = ""
        var  isLoggedIn = false
        var  firstLaunch = true
        var  email = ""
        var  uname = ""
        var  language = "en"
        var  id = 0
        var  token = ""
        var PreviouscatId:Int =0

    }



    object ApiKeys {
        const val LANG = "language"
        const val MOBILE = "mobile"
        const val AUTH = "auth"
        const val METHOD = "method"
        const val USERID = "userId"
        const val EMAIL = "email"
        const val NAME = "name"
        const val otp = "otp"
        const val deviceName = "deviceName"
        const val method = "method"
        const val DESC = "desc"
        const val SKILL = "skill"
        const val IMAGEURL = "imageUrl"
        const val isEmailVerified = "isEmailVerified"
        const val profileImage = "profileImage"
        const val address = "address"
        const val tagLine = "tagLine"
        const val description = "description"
        const val potfolio = "portfolio"
        const val education = "education"
        const val qualification = "qualification"
        const val skill = "skill"
        const val certificate = "certificate"
        const val title = "title"
        const val budget = "budget"
        const val TITLE = "title"
        const val TASKTYPE = "taskType"
        const val AMOUNT = "amount"
        const val DESCRIPTION = "description"
        const val DATE = "date"
        const val TIME = "time"
        const val LOCATION = "location"
        const val TASKID = "taskId"
        const val PACKAGEID = "packageId"
        const val BADGEID = "badgeId"
        const val ISPAID = "isPaid"
        const val OFFERID = "offerId"
        const val STATUS = "status"
        const val PAGENO = "pageNo"
        const val SIZE = "size"
        const val RATING = "rating"
        const val IMAGES = "images"
        const val REVIEW = "review"
        const val CATEGORYID = "categoryId"
        const val RECIVERID = "receiverID"
        const val SUBTYPE = "sub_Type"
        const val ADDSCOUNTS = "addsCount"
        const val VALIDITY = "validity"
        const val FORUSERID = "forUserId"
        const val REQUESTID = "requestId"

    }




    object Permission {

        const val CAMERA_PERMISSIONS = 201
        const val READ_STORAGE_PERMISSIONS = 202
        const val CAMERA_STORAGE_PERMISSIONS = 203
        const val LOCATION_PERMISSION = 204
        const val COURSE_LOCATION_PERSISSION = 205

        const val VIDEO_CALL_PERMISSION = 206
        const val AUDIO_CALL_PERMISSION = 207


        val CAMERA_PERM_LIST = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val READ_STORAGE_PERM_LIST = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val COURSE_LOCATION_PERM_LIST = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        val LOCATION_PERMISSION_PERMISSON_LIST = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val AUDIO_CALL_PERMISSION_LIST = arrayOf(Manifest.permission.RECORD_AUDIO)
        val VIDEO_CALL_PERMISSION_LIST =
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    }

    object RequestCode {

        const val RequestCode = "RequestCode"
        const val PLACEREQUESTCODE = 100
        const val CAMERA_INTENT = 101
        const val GALLERY_INTENT = 102
        const val Location_INTENT = 1

        const val ILLNESSREQUESTCODE = 104
        const val INSURANCEREQUESTCODE = 105

        const val GPS_REQUEST = 106
        const val LOCATION_REQUEST = 107
        const val PAYPAL_REQUEST_CODE = 108

    }



    //image Upload from amazon s3
    object AWS {
        const val POOL_ID: String = "ap-southeast-2:1e2edfcb-2a56-4d17-bb6b-179048746d30"
        const val BASE_S3_URL: String = "https://dykzryiex5xum.cloudfront.net/"
        const val ENDPOINT: String = "https://s3.ap-southeast-2.amazonaws.com/"
        const val BUCKET_NAME: String = "bilby-user-profile-dev"
    }

}