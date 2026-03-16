import base64
import os


class Manifest:
    path = os.path.dirname(__file__)

    def __init__(self,
                 encodedFileManifest: str = 'manifesteBase64.b',
                 decodedFileManifest: str = 'manifeste.json'
                 ):
        encodedManifest: str =None
        decodedManifest: str = None
        with open()
