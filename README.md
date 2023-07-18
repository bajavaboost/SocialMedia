
### @Service, @Repository, @Controller, (@Configuration) --> @Component
### @SpringBootApplication --> Component olan sınıflardan birer instance üretiyor.
### Ürettiği instance' ları arkada tarafta IoC Container' a uygun bir şekilde bir depoda tutuyor.
### Bu deponun adı --> applicationContext, SpringBootApplication, beanContainer

#### JDK vs JRE nedir?
#### JVM nedir? Java JVM ile nasıl çalışır?
#### Spring Stereo Type Annotation nedir? Anotasyonlar arasındaki farklar nelerdir?
#### @Configuration ve @Bean nedir?
#### ApplicationContext=SpringContainer nedir?
#### Spring de bean mantığı nedir? (Spring bean)
#### DI nedir? ne için kullanılır? tipleri nelerdir?
#### Autowired nedir? Neden cons inj. neden autowired?
#### SpringFramework vs SpringBoot?
#### SOLID prensipleri nelerdir?
#### RestAPI nedir? RESTful arch nedir?
### SOAP --> AP --> Access Protocol --> xml tabanlı veriler

## Http Request - JavaServlet Container
### Javada web işlemlerinde her şey http üzerinden yapılır ve temel olarak request <--> response şeklinde veri taşınır.
### ServletContainer --> Bir uygulamadaki http isteklerini alan, işleyen ve gönderilen cevabı göstermeyi sağlayan bir yapıdır.
### ServletContainer aslında bir sunucudur. Spring BOot' da bu sunucu gömülü olarak gelir. (Apache Tomcat)
### Sunucuya gelen istek DispatcherServlet tarafından alınır ve HandlerMapping' e iletilir.
### HandlerMapping gelen isteğin URL, Mapping(GET,POST vb.) gibi özelliklerine bakarak ilgili Controller metoduna isteği iletir.
### Dönen cevap DispatcherServlet tarafından yakalanır, sunucuya iletilir ve kullanıcıya bir cevap gönderilir.

### HandlerMapping işleminden sonra eğer uygulamada var ise Interceptors araya girer. --> Intercepters nedir?

# FeignClient
## FeignClient, HttpClient(Tomcat), RestTemplate --> Java da dışarıdaki farklı bir endpointe istek atmak için gerekli yapılar.
### RestTemplate --> Daha çok dışarıdaki bir API' dan veri çekmek istediğimizde kullanılırız. Sebebi daha esnek ve değiştirilebilir yöntemler sunuyor olmasıdır.
### HttpClient --> Basic Http Request --> Temel olarak RestTemplate ile aynı işi yapar.
### FeignClient --> Daha çok iki servis arasındaki isteklerde kullanılır. Yani microservice'ler arasındaki istekler için kullanılır.
### FeignClient, Spring Cloud ailesinin bir üyesidir. ve clouddaki uygulamada daha efektif ve etkin çalışabilir. Bu yüzden tercih edilir.

# -----------------------------------------------------------------------------------------------

### 1- MongoDB denemesi 
    Veritabanına bir kayıt yapılmalıdır. Bu işlem için DTO ve Mapper kullanınız.
### 2- UserProfile update işlemi 
    Register işlemi auth service' den feign client aracılığıyla yapıldı. Ancak userprofile service' de kullanıcının bazı bilgileri eksiktir.
    Eksik olan bilgilerin update metodu ile kullanıcıya tamamlatılması gerekmektedir. Bu işlem için gerekli metodu yazınız.
### 3- Auth update işlemi
    Update işleminde değiştirilen verilerin de auth service' e gönderilerek update edilmesi gerekmektedir.
    Bunun için UserprofileService' den AuthService' e bir manager yazılmalıdır. Ancak userprofile' dan auth' a veriyi gönderirken eşleştirme yapabilmek için
    bazı ek property'lere ihtiyaç vardır. UserProfile' da ve Auth' da gelen giden verilerde değişiklik yapmak gerekebilir.
    Bu işlem için gerekli manager ve service metotlarını yazınız.
### 4- ConfigServer 
    ConfigServer adında bir modül oluşturarak service .yml dosylarının konfigürasyonunu tamamlayınız.
### 5- Auth Delete işlemi
    Kullanıcının sistemden silinmesi gerekmektedir. Bu işlem için bir delete metodunu yazınız. 
    Bu metot kullanıcının statüsünü değiştirerek DELETED yapmalıdır.
# ----HAFTA SONU ÇALIŞMA----
### 6- UserProfile Delete işlemi
    Auth service' de yapılan delete işleminin userprofile service' e iletilöesi gerekmektedir. Open feign kullanarak bu işlemi gerçekleştiriniz.
### 7- AuthService' de activateStatus metodu
    Bu metotta düzenleme yapmalısınız. Yalnızca durumu PENDING olan kullanıcılar kod kullanarak durumunu ACTIVE yapabilir.
    Ayrıca bu işlemin UserProfile Service' e de gönderilmesi gerekmektedir. Oradaki durumunda değiştirilmesi gerekmektedir.
### 8- AuthService' de forgotPassword
    Kullanıcı sisteme giriş yapmak için kullandığı şifresini unutabilir. Bu şifreyi istediği zaman sıfırlayabilmelidir.
    Bunun için forgotPassword adında bir metot yazınız. Bu şifre değişikliğini UserProfile Service' e de gönderiniz.
### 9- Register işleminin RabbitMQ ile yapılması
    AuthService' de rabbitmq kullanabilmek için gerekli katmanların oluşturularak servis metoduna producer sınıfının çağırılması gerekmektedir.
    config --> rabbitmq.model --> rabbitmq.producer sıralaması ile katmanları doldurunuz.

