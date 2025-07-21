package com.g4br3.sitedentrodeapp.components

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable


@SuppressLint("ParcelCreator") // Você pode remover isso se implementar o CREATOR corretamente
data class AppData(val name: String, val data: String?) : Parcelable {

    // Construtor usado pelo CREATOR para ler do Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString()!!, // Lê o 'name'. O '!!' assume que 'name' nunca será nulo no Parcel.
        parcel.readString()    // Lê o 'data', que pode ser nulo.
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(data) // Pode escrever null strings diretamente
    }

    override fun describeContents(): Int {
        return 0 // Geralmente retorna 0, a menos que você esteja lidando com FileDescriptors.
    }

    companion object CREATOR : Parcelable.Creator<AppData> {
        override fun createFromParcel(parcel: Parcel): AppData {
            return AppData(parcel) // Chama o construtor que lê do Parcel
        }

        override fun newArray(size: Int): Array<AppData?> {
            return arrayOfNulls(size)
        }
    }
}
