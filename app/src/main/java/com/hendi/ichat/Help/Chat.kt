package com.hendi.ichat.Help

class Chat {
    var isi : String? = null
    var tanggal : String? =null
    var id : String? = null
    constructor(){}
    constructor(isi: String,tanggal: String,id : String){
        this.isi = isi
        this.tanggal = tanggal
        this.id = id
    }
}