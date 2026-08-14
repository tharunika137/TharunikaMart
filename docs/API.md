# API contract

Health:
GET `/api/v1/health`

Expected success envelope for future JSON endpoints:
```json
{"success":true,"data":{},"error":null}
```

Expected error envelope:
```json
{"success":false,"data":null,"error":{"code":"VALIDATION_ERROR","message":"..."}}
```
