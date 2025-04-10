# 🚀 Spring Boot + gRPC + Postman — No Protoc Setup Required

Want to get started with **gRPC in Spring Boot** without installing `protoc` or writing boilerplate manually? In this guide, I’ll walk you through building a simple gRPC server in Spring Boot (with Maven), and testing it using **Postman’s gRPC support** — no CLI setup needed!

---

## 💡 What You'll Learn

- How to integrate gRPC into a Spring Boot app using Maven
- How to auto-generate gRPC code without `protoc`
- How to test gRPC endpoints using Postman (no curl required!)
- How to fix `UNIMPLEMENTED` and common issues

---

## ⚙️ Tech Stack

- Spring Boot
- gRPC Java
- Maven (`protobuf-maven-plugin`)
- Postman

---

## 📦 Step 1: Set Up the Project

**Directory structure:**

```
src/
 └── main/
     ├── java/com/example/grpc/
     │    └── HelloServiceImpl.java
     └── proto/
          └── hello.proto
```

### `pom.xml`

Add dependencies:

```xml
<dependencies>
  <dependency>
    <groupId>net.devh</groupId>
    <artifactId>grpc-spring-boot-starter</artifactId>
    <version>2.13.1.RELEASE</version>
  </dependency>
</dependencies>
```

And add the plugin:

```xml
<plugin>
  <groupId>org.xolstice.maven.plugins</groupId>
  <artifactId>protobuf-maven-plugin</artifactId>
  <version>0.6.1</version>
  <configuration>
    <protocArtifact>com.google.protobuf:protoc:3.19.4:exe:${os.detected.classifier}</protocArtifact>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>compile</goal>
        <goal>compile-custom</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

---

## 📜 Step 2: Define `.proto`

Create `src/main/proto/hello.proto`:

```proto
syntax = "proto3";

package com.example.grpc;

service HelloService {
  rpc SayHello (HelloRequest) returns (HelloResponse);
}

message HelloRequest {
  string name = 1;
}

message HelloResponse {
  string message = 1;
}
```

---

## 🛠️ Step 3: Generate Code

Run:

```bash
./mvnw clean install
```

This will auto-generate code in:

```
target/generated-sources/protobuf
```

> 🔍 **IntelliJ Tip:**  
> Right-click the `target/generated-sources` folder →  
> **Mark Directory as → Generated Sources Root**  
> This makes `HelloServiceGrpc` importable inside your service class.

---

## 🔧 Step 4: Implement the gRPC Service

Create `HelloServiceImpl.java`:

```java
@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String message = "Hello, " + request.getName() + "! 👋";

        HelloResponse response = HelloResponse.newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```

✅ Use `@GrpcService` to register the gRPC service with Spring Boot.

---

## ▶️ Step 5: Run the App

```bash
./mvnw spring-boot:run
```

Ensure your app runs on port `9090`.

---

## 🔬 Step 6: Test with Postman (gRPC Tab)

1. Open **Postman**
2. Go to the **gRPC** tab
3. Use `localhost:9090` as the URL
4. Import your `hello.proto` file
5. Select:
   ```
   com.example.grpc.HelloService / SayHello
   ```
6. Payload:

```json
{
  "name": "Alice"
}
```

7. Hit **Invoke**

---

## ✅ Expected Output

```json
{
  "message": "Hello, Alice! 👋"
}
```

---

## 🧯 Troubleshooting

| Issue | Fix |
|-------|-----|
| `UNIMPLEMENTED` | Fully qualify the method: `com.example.grpc.HelloService/SayHello` |
| Can't import `HelloServiceGrpc` | Right-click `target/generated-sources` → mark as *Generated Source Root* |
| Proto not recognized in Postman | Use the *Service Definition* tab to import `.proto` |
| Nothing happens | Ensure gRPC is running on port `9090`, and the `.proto` is correct |

---

## 💼 gRPC Interview Questions (Java/Spring Focused)

Here are **7 commonly asked gRPC interview questions** (with quick hints):

1. **What is gRPC and why use it over REST?**  
   → gRPC uses HTTP/2 and protobufs for faster, more efficient communication — especially good for microservices.

2. **How does Spring Boot integrate with gRPC?**  
   → Using libraries like `grpc-spring-boot-starter`, which scans and registers services with `@GrpcService`.

3. **What’s the difference between Unary, Streaming, and Bi-Directional gRPC?**  
   → Unary: 1 request–1 response.  
   → Streaming: one side sends multiple messages.  
   → BiDi: both sides stream.

4. **How are gRPC services registered in Spring Boot?**  
   → With `@GrpcService`, not `@Service`. This ensures it's picked up by the gRPC server.

5. **Where are gRPC Java classes generated from `.proto` files?**  
   → Maven plugin places them under `target/generated-sources/protobuf`.

6. **Can gRPC work over a browser?**  
   → Not directly (no browser-native HTTP/2 trailers), but with gRPC-Web you can.

7. **What are some common issues when using Postman for gRPC?**  
   → Not importing `.proto`, missing fully-qualified service name, or using wrong payload format.

---

## 🙌 Final Thoughts

With just a few steps, you’ve got gRPC running in Spring Boot — and tested through Postman, *no CLI required*. Fast, type-safe, and modern!

---