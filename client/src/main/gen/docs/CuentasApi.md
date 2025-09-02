# CuentasApi

All URIs are relative to *http://localhost:8082*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**cuentasGet**](CuentasApi.md#cuentasGet) | **GET** /cuentas | Listar cuentas |
| [**cuentasIdDelete**](CuentasApi.md#cuentasIdDelete) | **DELETE** /cuentas/{id} | Eliminar cuenta |
| [**cuentasIdDepositarPut**](CuentasApi.md#cuentasIdDepositarPut) | **PUT** /cuentas/{id}/depositar | Depositar a la cuenta |
| [**cuentasIdGet**](CuentasApi.md#cuentasIdGet) | **GET** /cuentas/{id} | Obtener cuenta por id |
| [**cuentasIdRetirarPut**](CuentasApi.md#cuentasIdRetirarPut) | **PUT** /cuentas/{id}/retirar | Retirar de la cuenta |
| [**cuentasPost**](CuentasApi.md#cuentasPost) | **POST** /cuentas | Crear cuenta |


<a id="cuentasGet"></a>
# **cuentasGet**
> List&lt;BankAccount&gt; cuentasGet()

Listar cuentas

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    try {
      List<BankAccount> result = apiInstance.cuentasGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;BankAccount&gt;**](BankAccount.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Lista de cuentas |  -  |

<a id="cuentasIdDelete"></a>
# **cuentasIdDelete**
> cuentasIdDelete(id)

Eliminar cuenta

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.cuentasIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | Eliminada |  -  |
| **404** | No encontrado |  -  |

<a id="cuentasIdDepositarPut"></a>
# **cuentasIdDepositarPut**
> BankAccount cuentasIdDepositarPut(id, amountRequest)

Depositar a la cuenta

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    Long id = 56L; // Long | 
    AmountRequest amountRequest = new AmountRequest(); // AmountRequest | 
    try {
      BankAccount result = apiInstance.cuentasIdDepositarPut(id, amountRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasIdDepositarPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |
| **amountRequest** | [**AmountRequest**](AmountRequest.md)|  | |

### Return type

[**BankAccount**](BankAccount.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Saldo actualizado |  -  |
| **400** | Error de negocio |  -  |

<a id="cuentasIdGet"></a>
# **cuentasIdGet**
> BankAccount cuentasIdGet(id)

Obtener cuenta por id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BankAccount result = apiInstance.cuentasIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**BankAccount**](BankAccount.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Cuenta |  -  |
| **404** | No encontrado |  -  |

<a id="cuentasIdRetirarPut"></a>
# **cuentasIdRetirarPut**
> BankAccount cuentasIdRetirarPut(id, amountRequest)

Retirar de la cuenta

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    Long id = 56L; // Long | 
    AmountRequest amountRequest = new AmountRequest(); // AmountRequest | 
    try {
      BankAccount result = apiInstance.cuentasIdRetirarPut(id, amountRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasIdRetirarPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |
| **amountRequest** | [**AmountRequest**](AmountRequest.md)|  | |

### Return type

[**BankAccount**](BankAccount.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Saldo actualizado |  -  |
| **400** | Regla de negocio (ahorros &gt;&#x3D; 0; corriente &gt;&#x3D; -500) |  -  |

<a id="cuentasPost"></a>
# **cuentasPost**
> BankAccount cuentasPost(createAccountRequest)

Crear cuenta

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CuentasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8082");

    CuentasApi apiInstance = new CuentasApi(defaultClient);
    CreateAccountRequest createAccountRequest = new CreateAccountRequest(); // CreateAccountRequest | 
    try {
      BankAccount result = apiInstance.cuentasPost(createAccountRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CuentasApi#cuentasPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **createAccountRequest** | [**CreateAccountRequest**](CreateAccountRequest.md)|  | |

### Return type

[**BankAccount**](BankAccount.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Cuenta creada (saldo inicial 0.0) |  -  |
| **400** | Error de negocio o validación |  -  |

