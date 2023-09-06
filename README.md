# HLSSample

功能：播放HLS streaming

說明：
使用ExoPlayer版本：2.16.1
利用按鈕連結onClickPlaybackByMediaItem/onClickPlaybackByMediaSource區分要播放的媒體類型：
1.PlaybackByMediaItem
    播放沒有signed過的url, 不帶cookie
2.PlaybackByMediaSource
    播放signed過的url, 帶cookie
兩者皆能在輸入框輸入不同的m3u8網址播放。
刪除不必要的程式碼，用最精簡的幾行程式達到單一播放功能(移除playlist)。

注意事項：
1. SimpleExoPlayer在新版本已被移除
2. DefaultHttpDataSourceFactory在新版被改為DefaultHttpDataSource.Factory

Reference:
https://zoejoyuliao.medium.com/%E7%94%A8-aws-lambda-aws-mediaconvert-%E5%AF%A6%E7%8F%BE%E5%BD%B1%E7%89%87%E8%BD%89%E6%AA%94%E8%88%87%E4%B8%B2%E6%B5%81-%E4%B8%80-%E4%BB%80%E9%BA%BC%E6%98%AF%E4%B8%B2%E6%B5%81%E8%88%87-hls-72c8a7b9201

Test unsigned url:
https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8
https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8

Test signed url(need cookie):
https://dthl9qxmc6c3r.cloudfront.net/playback.m3u8
Cookie="CloudFront-Key-Pair-Id=KLF6VW5TBETCE;CloudFront-Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9kdGhsOXF4bWM2YzNyLmNsb3VkZnJvbnQubmV0LyoiLCJDb25kaXRpb24iOnsiRGF0ZUxlc3NUaGFuIjp7IkFXUzpFcG9jaFRpbWUiOjE2NzEyNjQyMzl9fX1dfQ__;CloudFront-Signature=GfcTLRv~H0JH~-V0PIatS0hIX7YV5fQ8S-CIl5Cbz4cbm30QgbUDBmEu~4H4Hs7ywAR9TDzJfp~uJtbSXGZ0fJGdjfne~ReZ7EOjuZsFVhZcd1Gzt6-r5znEu~S8oKKFo-GI9iAVvwzn~Q2vkr9iNqKyvADFpydhNc06LOYwxL6vEMAfCVXyAhFdd70VKILxWnAP-JVrBZH-iSYfxe3goPFJFqXB1owL1v6mwK1SQhIf64kTBZfeIoBYoY9QBhmzLdPlesCjBuIX11H0XmtPquUJICTZmyUPLkg4sfHaMPw4xaQbmjP2y8mqgkF7xLz2up7HnFaUMKSRen3aluuDPw__"
