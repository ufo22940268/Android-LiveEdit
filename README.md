Android-WebEditText
===================

A EditText which can input text content with pc web brower.
Sometimes when you need to input a artical in android device, it's easily be disturbed by new incoming call or something else. and also the android keyboard's input efficiency is quit low to pc keyboard.
So `WebEditText` will delegate the input task to your pc, and when completed the text will be sent to your device.

Screenshot
=====
![](./slide2.gif)

Usage
=====

**Step1:**

Import `library` module to your project.

**Step2:**

Use WebEditText in your xml file like this:

    <com.bettycc.webedittext.library.WebEditText
        android:id="@+id/edit_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
        

**Step3:**

Use [WebEditText.stopServer](https://github.com/ufo22940268/Android-WebEditTextView/blob/master/library/src/main/java/com/bettycc/webedittext/library/WebEditText.java#L146) whenever you need to shut down the server.

Demo download
=====
[link](http://pan.baidu.com/s/1jGJwzlG)
