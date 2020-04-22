package com.hendi.ichat.Help

class User  {
    var nama : String? = null
    var email : String?=null
    var password : String? = null
    var image : String? = null
    constructor(){}
    constructor(nama: String,email: String,password: String,image: String){
        this.nama = nama
        this.email = email
        this.password = password
        this.image = image
    }
}