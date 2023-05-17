let oEditors = []

    smartEditor = function() {
      console.log("Naver SmartEditor")
      nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "smartEditor",
        sSkinURI: "smarteditor/SmartEditor2Skin.html",
        fCreator: "createSEditor2"
      })
    }

    $(document).ready(function() {
      smartEditor()
    })
