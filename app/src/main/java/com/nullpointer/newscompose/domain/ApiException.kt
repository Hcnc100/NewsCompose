package com.nullpointer.newscompose.domain

import java.lang.Exception

class ApiException(errorCode:String): Exception("Error code : $errorCode")