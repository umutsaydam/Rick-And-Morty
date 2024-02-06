# Rick-And-Morty

## ScreenShots
![coverAPP](https://github.com/umutsaydam/Rick-And-Morty/assets/69711134/4630db62-b8b3-4bcf-94b4-3192f4e91ef3)

<div>
<h3><a href="#uygulama-icerik">TR</a></h3>
<h3><a href="#app-content">EN</a></h3>
</div>

## TR
### <p id="uygulama-icerik"></p>
Uygulama Özellikleri
 - Rick ve Morty karakterleri listeler.
 - Karakterler hakkında detaylı bilgi sunar.
 - Favori karakterleri kaydetme özelliği mevcut.
 - Açık/ Karanlık tema özelliği.
 - Türkçe ve İngilizce dil desteği

## Kullanılan Teknolojiler - Kütüphaneler
- Kotlin
- Coroutines - Main thread'leri bloklayıp uygulamaları yanıt vermemesine sebep olabilecek işlemleri asenkron bir şekilde arka planda yapmamızı sağlar.
- JetPack
    - LiveData - Verileri gözlemlenebilir olmasını sağlar.
    - Lifecycle 
    - ViewModel - MVVM mimaresinin bir parçası olan view model, mantıksal işlemlerin yürütüldüğü kısımdır.
    - Room - Temeli SQLite olan veri tabanı. 
    - Navigation - Fragment'lar arası dolaşım ve parametre gönderimi için kullanıldı.
    - Data Binding - XML layout'larda yer alan komponentlerin kod kısmından verimli bir şekilde erişebilmesini sağlar. 
- Material-Components - Material design komponentler (cardView gibi).
- Retrofit - REST API hizmeti için kullanıldı.
- Glide - Resimleri verimli bir şekilde gösterilmesini sağlar.

## <p id="app-content">TR</p>
App Features
  - List of Rick and Morty characters
  - Detail of characters
  - Bookmark character
  - Light/ Dark theme.
  - Turkish and English languages are supported

## Tech Stack - Library
- Kotlin
- Coroutines - Allows us to perform operations asynchronously in the background, which may block main threads and cause applications to stop responding.
- JetPack
    - LiveData - It makes the data observable.
    - Lifecycle
    - ViewModel - The view model, which is a part of the MVVM architecture, is the part where logical operations are carried out.
    - Room - Database based on SQLite.
    - Navigation - Used for inter-fragment navigation and parameter sending.
    - Data Binding - It enables the components in XML layouts to be accessed efficiently from the code section.
- Material-Components - Material design components (like cardview).
- Retrofit - Used for REST API service.
- Glide - It allows images to be displayed efficiently.
