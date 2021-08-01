# URL:/test/one

这是接口描述

**请求成功返回数据字段描述:**

| 返回字段 | 类型 | 示例值 | 描述 |
| --- | --- | :--- | :--- |
| jsapi_ticket | 字符串 | sM4AOVdWfPE4DxkXGEs8VCRoJVk67rr-iW2UdUZ0FlGeu8g_e--aERHgGizxWsqGpiGR2in53Y-ZzpnFRsiNrQ | 根据access_token从微信获取的ticket |
| noncestr | 字符串 | y8lv4oj9xq | 随机数 |
| signature | 字符串 | dd2e8f46dca97e0c60effa1ed3beba7fc5851b65 | 根据ticket等参数从微信获取的签名 |
| timestamp | 字符串 | 1582025626 | 时间戳 |
| url | 字符串 | http://mp.weixin.qq.com?params=value | 客户端传递的参数，返回给客户端 |


**请求成功返回的数据基本示例:**

```json
{
    "result": {
        "jsapi_ticket": "sM4AOVdWfPE4DxkXGEs8VCRoJVk67rr-iW2UdUZ0FlGeu8g_e--aERHgGizxWsqGpiGR2in53Y-ZzpnFRsiNrQ",
        "noncestr": "y8lv4oj9xq",
        "signature": "dd2e8f46dca97e0c60effa1ed3beba7fc5851b65",
        "timestamp": "1582025626",
        "url": "http://mp.weixin.qq.com?params=value"
    },
    "code": 200,
    "message": "success"
}
```

---

code:200
**请求成功返回的数据**
​```json
    {
        "code": 100,
        "message": "",
        "result":"name='TrueDei'"
    }
​```
---


code:400
**请求失败返回的数据**
​```json
    {
        "code": 400,
        "message": "访问失败了",
        "result":""
    }
​```
---
