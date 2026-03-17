package com.g4br3.sitedentrodeapp.recicleView

import com.g4br3.sitedentrodeapp.R

open class Icon(var value: Int)

class Default(
): Icon(R.mipmap.ic_document)


class Audio(
): Icon(R.mipmap.ic_audio)


class Image(
): Icon( R.mipmap.ic_image)


class Folder(
): Icon(R.mipmap.ic_folder_empty)


class Video(
): Icon(R.mipmap.ic_video)
