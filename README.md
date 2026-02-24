
# 通貨変換アプリ（Spring Boot + MySQL + Docker）

外部 API から取得した通貨レートをユーザごとに保存し、最新レートを使って金額変換ができる Web アプリです。  
Spring Boot を中心に、認証・DB 設計・API連携・Docker による環境構築まで実装しています。
---

## 開発背景
Spring Boot の理解を深めるため、以下の要素を含む Web アプリを制作しました。

 - 認証機能 (Spring Security）  
 - DB 永続化（Spring Data JPA）  
 - 外部 API 連携  
 - ユーザ単位でのデータ分離  
 - 管理者機能（ユーザ管理・レート管理）  
 - Docker によるコンテナ環境構築  
 - DTO / Mapper によるレイヤードアーキテクチャ  
---

## 主な機能
### 認証・ユーザ管理
 - ユーザー登録・ログイン（Spring Security）  
 - ロール管理（ROLE_USER / ROLE_ADMIN）  
 - 管理者ユーザの自動生成（CommandLineRunner）  
 
### 通貨変換 
 - 外部 API から通貨レート取得  
 - 1時間以内のﾚｰﾄは DB を再利用（API 呼び出し削減）  
 - 金額変換（例：USD → JPY）  
 - 変換結果をユーザごとに自動保存  
 
### 履歴管理
 - ユーザーごとのレート履歴表示（ページング対応）  
 - 履歴の論理削除（削除日時・削除者を記録）  
 - 削除前の確認ダイアログ  
 
### 管理者専用画面
 - 全ユーザ一覧表示・削除（※管理者は削除不可）  
 - 全レート一覧表示  
 - レートの論理削除 / 完全削除（物理削除）  
 ---
 
## 使用技術

| 分類 | 技術 |
| ------------ | ----------- |
| 言語 | Java 21 |
| フレームワーク | Spring Boot |
| 認証 | Spring Security |
| ORM | Spring Data JPA |
| DB | MySQL 8.0（Docker）|
| テンプレート | Thymeleaf |
| コンテナ | Docker |
---

## アーキテクチャ構成

Controller → Service → Repository → MySQL   
DTO / Mapper を導入し、 Controller が Entity を直接扱わない設計。 

### Docker 構成
 - `currency-app`（Spring Boot）  
 - `currency-mysql`（MySQL 8.0）  
 アプリと DB は Docker Compose により同一ネットワークで接続。  
 コンテナ間通信は `localhost` ではなく **サービス名で接続する設計** を採用しています。
 
 
## 技術的な工夫
  
### ①  API 制限対策 

１時間以内に取得済みのレートが存在する場合は外部 API を呼ばす、DB の値を利用。  
→ API 使用回数削減 & パフォーマンス向上  

### ② ユーザ単位のデータ管理
認証ユーザ名をキーとして保存することで、ユーザごとのデータ分離を実現。  

### ③ 論理削除の導入  
・`deleted` / `deletedAt` / `deletedBy` を追加  
・管理者画面では削除済みも表示  
・ユーザ画面では削除済みを除外  
 
### ④ Docker 環境での問題解決
・Controller は DTO のみ扱う  
・Entity → DTO 変換は Mapper に集約  
・表示用フォーマット（日時など）も Mapper に統一  

### ⑤ Docker 環境での問題解決
開発中に発生した問題：  
・Hibernate Dialect エラー  
・コンテナ内から`localhost`接続できない問題  
・Maven parent POM 解決エラー  
→ ログ解析・ネットワーク理解により解決しました。  


## アプリ全体構成図

<img src="./screenshots/dfd.png" width="700">

### ER 図

<img src="./screenshots/erd.png" width="600">

## 画面キャプチャ

### ログイン画面
ユーザー名とパスワードでログインします。  
<img src="./screenshots/login.png" width="450">

### 通貨変換画面
基準通貨・対象通貨・金額を入力して変換を行います。  
<img src="./screenshots/exchange.png" width="500">

### レート履歴画面
ユーザごとに保存された変換履歴を一覧表示します。  
<img src="./screenshots/history.png" width="600">

---

## セットアップ手順

### 1. リポジトリをクローン   
```bash
git clone https://github.com/Hazuki120/exchange.git
```

### 2. `.env`ファイル作成
```env
MYSQL_ROOT_PASSWORD=rootpass
MYSQL_DATABASE=exchange
MYSQL_USER=appuser
MYSQL_PASSWORD=apppass

SPRING_DATASOURCE_USERNAME=appuser
SPRING_DATASOURCE_PASSWORD=apppass

EXCHANGE_API_KEY=your_api_key
```
⚠ Windows の環境変数に同名キーがあると `.env` が無視されるので注意。  
### 3. Docker で MySQL を起動
#### 起動
```bash
docker compose up --build
```

### 4. アクセス方法
アプリ起動後、ブラウザで以下にアクセスしてください。  

http://localhost:8080  
#### 停止
```bash
docker compose down
```

## API エンドポイント一覧
| メソッド | パス | 説明 |
|----------|------|------|
| GET | /login | ログイン画面 |
| GET | /signup | ユーザ登録画面 |
| GET | /exchange | 通貨変換フォーム |
| GET | /exchange/history | ユーザのレート履歴 |
| POST | /exchage/history/delete | 履歴削除 |
| GET | /admin/rates | 管理者全レート表示 |
| POST | /admin/rates/delete | レート論理削除 |
| POST | /admin/rates/hard-delete | 管理者：レート完全削除 |
| GET | /admin/users | 管理者：ユーザ一覧 |
| POST | /admin/users/delete | 管理者：ユーザ削除 |

## ディレクトリ構成
```text
src/  
 └─ main/  
     ├─ java/  
     │   └─ com/example/exchange/  
     │        ├─ application/  
     │        │    ├─ controller/  
     │        │    │    ├─ AdminController.java  
     │        │    │    ├─ CurrencyController.java  
     │        │    │    ├─ HistoryController.java  
     │        │    │    ├─ LoginController.java  
     │        │    │    └─ SignUpController.java  
     │        │    └─ config/  
     │        │         ├─ CustmoLoginSuccessHandler.java  
     │        │         └─ DataIntializer.java  
     │        │         └─ SecurityConfig.java  
     │        ├─ domain/  
     │        │    ├─ model/  
     │        │    │    ├─ CurrencyRate.java  
     │        │    │    └─ User.java  
     │        │    ├─ repository/  
     │        │    │    ├─ CurrencyRateRepository.java  
     │        │    │    └─ UserRepository.java  
     │        │    ├─ service/  
     │        │    │    ├─ CurrencyConversionService.java  
     │        │    │    ├─ CurrencyRateService.java  
     │        │    │    ├─ CustomUserDetailsService.java  
     │        │    │    └─ UserService.java  
     │        └─ infrastructure/  
     │             └─ CurrencyAppApplication.java  
     └─ resources/  
         ├─ static/  
         ├─ templates/  
         │    ├─ exchange.html  
         │    ├─ history.html  
         │    ├─ login.html  
         │    ├─ result.html  
         │    ├─ signup.html  
         │    └─ admin  
         │         ├─ rates.html  
         │         └─ users.html  
         └─ application.properties  
```

## 今後の課題
 - API レート制限対策（キャッシュ強化）
 - グラフ表示（レート推移）
 - 管理者画面の UI 改善
 - テストコードの追加（JUnit / Mockito）
 
---
## このアプリで学んだこと
 - Spring Security の認証フロー理解
 - 外部 API 連携の実装方法
 - Docker による開発環境構築
 - コンテナ間通信の考え方
 - DTO / Mapper による責務分離
 - 論理削除の設計と実装
 
 

 
 
 