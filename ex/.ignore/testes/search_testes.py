def search_module():
    from   os import path
    import  sys
    import  os
    # search module
    sys.path.append(path.dirname(__file__).replace("testes", "utils"))
    from search import Search  # type: ignore
    return Search(
        os.listdir(),
        __file__
    )


search = search_module()