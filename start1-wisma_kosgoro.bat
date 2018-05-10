rem java -jar dist/smart-cctv.jar "http://114.110.17.6:8896/image.jpg?type=motion" "Wisma Kosgoro, MH Thamrin"
java -cp target/smart-cctv-service-1.0-SNAPSHOT.jar com.mandelag.smartcctv.services.MainCCTVService localhost 9095 "http://114.110.17.6:8896/image.jpg?type=motion" .\src\main\java\res\cars.xml

rem http://202.51.112.91:676/image2