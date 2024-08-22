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
│   ├── LoggerUtils.kt\n
│   ├── PrettyJsonLogger.kt\n
│   ├── RetrofitClient.kt\n
│   ├── model
│   │   ├── BaseResponse.kt
│   │   ├── MessageResponseDTO.kt
│   │   ├── auth
│   │   │   ├── GetAccessTokenRequestDTO.kt
│   │   │   ├── GetAccessTokenResponseDTO.kt
│   │   │   └── Information.kt
│   │   ├── calendar
│   │   │   ├── AddFutureQuestRequestDTO.kt
│   │   │   ├── GetMonthExpResponseDTO.kt
│   │   │   └── GetMonthExpResponseDTOItem.kt
│   │   ├── character
│   │   │   ├── ChangeNicknameRequestDTO.kt
│   │   │   ├── ChangeNicknameResponseDTO.kt
│   │   │   ├── CharacterInfoResponseDTO.kt
│   │   │   ├── GetCharacterItemResponseDTO.kt
│   │   │   ├── GetInitialResponseDTO.kt
│   │   │   ├── ItemChangeResponseDTO.kt
│   │   │   ├── MakeInitCharacterDTO.kt
│   │   │   ├── MyCharacterEquipItemDetailReq.kt
│   │   │   └── MyCharaterDetailRes.kt
│   │   ├── item
│   │   │   ├── CategoryItem.kt
│   │   │   └── ItemDTO.kt
│   │   ├── quest
│   │   │   ├── AddQuestRequestDTO.kt
│   │   │   ├── CompleteQuestResponseDTO.kt
│   │   │   ├── MainResponseDTO.kt
│   │   │   ├── QuestResponseDTO.kt
│   │   │   └── UpdateQuestRequestDTO.kt
│   │   └── user
│   │       ├── IsUserRegisteredResponseDTO.kt
│   │       ├── UserEmailResponseDTO.kt
│   │       └── WithdrawResponseDTO.kt
│   ├── repository
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── CalendarRepositoryImpl.kt
│   │   ├── CharacterRepositoryImpl.kt
│   │   ├── DataStoreRepositoryImpl.kt
│   │   ├── ItemChangeRepositoryImpl.kt
│   │   ├── ItemRepositoryImpl.kt
│   │   ├── QuestRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   └── service
│       ├── AuthService.kt
│       ├── CalendarService.kt
│       ├── CharacterService.kt
│       ├── ItemChangeService.kt
│       ├── ItemService.kt
│       ├── KakaoAuthService.kt
│       ├── QuestService.kt
│       └── UserService.kt
├── domain
│   ├── model
│   │   ├── IsUserRegisteredInfo.kt
│   │   ├── ItemInfo.kt
│   │   ├── LoginResponseEntity.kt
│   │   ├── MessageInfo.kt
│   │   ├── calendar
│   │   │   ├── GetMonthExpInfo.kt
│   │   │   ├── GetMonthExpInfoItem.kt
│   │   │   └── MonthQuestInfo.kt
│   │   ├── character
│   │   │   ├── CharacterInitialInfo.kt
│   │   │   ├── GetCharacterItemInfo.kt
│   │   │   ├── MyCharacterDetailInfo.kt
│   │   │   └── MyPageInfo.kt
│   │   ├── home
│   │   │   ├── HomeExpInfo.kt
│   │   │   └── ItemData.kt
│   │   └── quest
│   │       ├── AddQuestInfo.kt
│   │       ├── CompleteQuestInfo.kt
│   │       └── QuestInfo.kt
│   └── repository
│       ├── AuthRepository.kt
│       ├── CalendarRepository.kt
│       ├── CharacterRepository.kt
│       ├── DataStoreRepository.kt
│       ├── ItemChangeRepository.kt
│       ├── ItemRepository.kt
│       ├── QuestRepository.kt
│       └── UserRepository.kt
└── presentation
    ├── UiState.kt
    ├── base
    │   └── GlobalApplication.kt
    └── views
        ├── MainActivity.kt
        ├── calendar
        │   ├── CalendarFragment.kt
        │   ├── CalendarViewModel.kt
        │   ├── DayAdapter.kt
        │   └── MonthAdapter.kt
        ├── character
        │   └── CharacterViewModel.kt
        ├── characterSetting
        │   ├── CharacterSettingActivity.kt
        │   ├── CharacterSettingViewModel.kt
        │   ├── ClothesSettingFragment.kt
        │   ├── FaceSettingFragment.kt
        │   ├── HairSettingFragment.kt
        │   ├── NameSettingFragment.kt
        │   ├── SettingCompleteFragment.kt
        │   └── SkinSettingFragment.kt
        ├── home
        │   ├── HomeFragment.kt
        │   ├── HomeViewModel.kt
        │   └── QuestRvAdapter.kt
        ├── item
        │   ├── GridSpacingItemDecoration.kt
        │   ├── ItemFragment.kt
        │   ├── ItemRvAdapter.kt
        │   └── ItemViewModel.kt
        ├── mypage
        │   ├── MyPageFragment.kt
        │   ├── MyPageViewModel.kt
        │   └── SettingFragment.kt
        ├── signIn
        │   ├── SignInActivity.kt
        │   └── SignInViewModel.kt
        └── splash
            ├── SplashActivity.kt
            └── SplashViewModel.kt
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
