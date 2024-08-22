# 3rd-MVP-NoPlanB-Android

## 멤버
[김하은](https://github.com/shong9124)<p>
[함범준](https://github.com/HamBeomJoon)

----
![DEPth_DemoDay (1)2_Image](https://github.com/user-attachments/assets/1120b797-a403-4820-ad62-b8041a57262c)
![페이지 범위 DEPth_DemoDay (1)_Image](https://github.com/user-attachments/assets/fa0cc1b6-9e9e-44dd-9af7-1dd808f61954)

![페이지 범위 DEPth_DemoDay (1)_Image2](https://github.com/user-attachments/assets/e1552867-571e-4c73-bfce-0fbe91da35fc)

![페이지 범위 DEPth_DemoDay (3)_Image](https://github.com/user-attachments/assets/dd6bbcec-85a2-4ac3-bb0f-49a324c1c5c6)

## ⚙️ Android App Architecture

## 🗂️ Package Structure
```markdown
├── data
│   ├── model
│   │   ├── auth
│   │   ├── calendar
│   │   ├── character
│   │   ├── item
│   │   ├── quest
│   │   └── user
│   ├── repository
│   └── service
├── domain
│   ├── model
│   │   ├── calendar
│   │   ├── character
│   │   ├── home
│   │   └── quest
│   └── repository
└── presentation
    ├── base
    └── views
        ├── calendar
        ├── character
        ├── characterSetting
        ├── home
        ├── item
        ├── mypage
        ├── signIn
        └── splash
```

## 🛠️ 기술 스택
| **분류** | **내용** |
| --- | --- |
| **로컬 데이터 저장** | DataStore |
| **네트워크 통신** | OkHttp3, Retrofit2 |
| **오브젝트 매핑** | Gson |
| **로깅** | Orhanobut:logger, Okhttp3:logging-interceptor |
| **카카오 로그인** | Kakao SDK |
| **쓰레드 처리** | Kotiln Coroutine |
| **이미지, GIF 캐싱 및 로드** | Glide |
| **뷰 관련** | Fragment, Activity |
| **상태 관리** | ViewModel, LiveData |
