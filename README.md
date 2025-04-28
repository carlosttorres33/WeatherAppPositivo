Prueba técnica para PositivoS+, en la Cual se ponen en práctica todos los puntos requeridos para poder mostrar la información del clima que el usuario desee a través de un buscador y pueda seguir haciendolo de manera offline

Se utiliza Retrofit para el consumo de la api de OpenWeatherMap para poder obtener la información del clima de una ubicación en especial en base a la Latitud y Longitud enviadas como parámetro en la petición

Al inicio la app cargara la informacion del clima de la ubicacion actual del usuario en caso de que este otorgue los permisos necesarios

Se cuenta con una barra de busqueda que, mediante la api de GooglePlaces ayuda a encontrar lugares en especifico de Mexico y el mundo para buscar su informacion del clima

En la parte inferior se muestra un mapa el cual navega hacia la ubicacion seleccionada por el usuario en el buscador

Cuando la conexión a internet no esté disponible el usuario podrá continuar consultando la información de los lugares que ya se hayan consultado y almacenado localmente en el dispositivo mediante una Base de Datos con Room

Se muestra un Loader cada vez que se estén realizando consultas

Mediante el uso de BindingAdapters la UI se actualiza con la nueva información recibida



El apk para instalar y probar la aplicacion se encuentra en la seccion de Releases, de igual manera adjunto el link para poderla descargar: (https://github.com/carlosttorres33/WeatherAppPositivo/releases/download/V1.2/WeatherApp1.2.0.apk)

Asi como evidencia de la funcionalidad de la aplicacion y una pequeña descripción de los flujos

Se obtiene el clima de la ubicación del usuario al iniciar la aplicación
![MyLocationWeather](https://github.com/user-attachments/assets/49228b1d-9e2c-4d9a-930d-57650f5a31a6)

Buscador de ubicaciones
![ShowPlaceSearcher](https://github.com/user-attachments/assets/96cb3d50-a1ef-468c-8f89-40dc1b5d3441)

Loader cuando se está realizando la petición y carga la información obtenida de cuantos lugares el usuario desee
![LoadingPlaceSelectedWeather](https://github.com/user-attachments/assets/ddd7686f-1dd5-4df2-811d-84a505b46244)
![PlaceSelectedWeatherLoaded](https://github.com/user-attachments/assets/6682cf66-2165-4210-9c5a-5265e57bb5cc)
![NewZeelandWeather](https://github.com/user-attachments/assets/1d030811-8973-473a-9a50-9cb59dfdbfe6)
![Rekiavik](https://github.com/user-attachments/assets/74094698-5428-4d3c-a072-b93cd88443b7)
![SantiagoBernabeuWeather](https://github.com/user-attachments/assets/526b0e67-2898-46cc-997d-f01ea1d33118)

Cuando no hay internet se muestra un pequeño snackbar para notificar la falta de conexión
![NotInternetSnackBar](https://github.com/user-attachments/assets/e2160e0f-3a4d-4f06-9e32-690e9c2d58d6)

Sin conexión se despliega un BottomSheet para buscar información de lugares almacenados localmente
![LocalWeatherBS](https://github.com/user-attachments/assets/af588b76-6317-4bdb-a74d-ec6292498aa5)
![LocalSearch](https://github.com/user-attachments/assets/78dc8cba-8f2f-416d-b4d5-af2c264e24ba)

Cuando se selecciona un lugar offline, de igual manera la interfaz y el mapa se actualizan
![OfflinePlaceWeather](https://github.com/user-attachments/assets/844d1834-bb4c-46d1-87b0-14400843dfaf)

