# security_oauth2.0_demo
oauth2.0 示例 

可启用的最精简的资源端和认证端

* 获取授权码

  http://127.0.0.1:9998/oauth/authorize?response_type=code&client_id=auth_demo&redirect_uri=https://www.baidu.com

* 换取token

  http://127.0.0.1:9998/oauth/token

  **form**

  | grant_type       | authorization_code               |
  | ---------------- | -------------------------------- |
  | code             | AJ1ROw                           |
  | **redirect_uri** | **https://www.baidu.com** |
  | **client_id**    | **auth_demo**                    |

  **basic_auth**

  auth_demo:123456

* 访问资源

  http://127.0.0.1:9999/info

  **bearer token**

  `6b6c46c0-9f98-4735-93b3-7df738c8745f`

​		
