# Simple QR Reader App

Una aplicación nativa para Android que permite escanear códigos QR usando la cámara del dispositivo. La aplicación lee inmediatamente el código QR, muestra el resultado y ofrece la opción de abrir el enlace en el navegador predeterminado si se detecta una URL. Además, guarda un historial de los últimos 10 escaneos.

## Características

- Escaneo de códigos QR usando la cámara del dispositivo
- Lectura automática de códigos QR al detectarlos
- Escaneo de códigos QR desde imágenes guardadas en la galería
- Apertura de URLs en el navegador predeterminado
- Historial de los últimos 10 escaneos
- Opciones para copiar y compartir resultados
- Interfaz de usuario moderna e intuitiva

## Requisitos

- Android Studio
- Android SDK con nivel de API 21 (Android 5.0) o superior
- Dispositivo Android con cámara o emulador con cámara virtual

## Cómo probar la aplicación en tu dispositivo

1. **Configurar tu dispositivo para desarrollo**:
   - Habilita las "Opciones de desarrollador" en tu dispositivo Android:
     - Ve a Configuración > Acerca del teléfono
     - Toca 7 veces sobre "Número de compilación" para habilitar las opciones de desarrollador
   - Habilita la "Depuración USB" en Opciones de desarrollador
   - Conecta tu dispositivo a tu ordenador mediante USB
   - Asegúrate de conceder los permisos cuando se te solicite en el dispositivo

2. **Abrir el proyecto en Android Studio**:
   - Abre Android Studio
   - Selecciona "Open an existing Android Studio project"
   - Navega hasta la carpeta del proyecto QR Reader y selecciónala

3. **Compilar y ejecutar la aplicación**:
   - Selecciona tu dispositivo en la lista de dispositivos disponibles
   - Haz clic en el botón "Run" (triángulo verde) o presiona Shift+F10
   - La aplicación se instalará y se ejecutará en tu dispositivo
   - Concede los permisos de cámara cuando se te solicite

## Publicar la aplicación en Google Play Store

1. **Preparar la app para producción**:
   - Actualiza el `versionCode` y `versionName` en el archivo `app/build.gradle`
   - Asegúrate de que la app cumpla con las [Políticas del Programa para Desarrolladores de Google Play](https://play.google.com/about/developer-content-policy/)
   - Elimina cualquier código de depuración o logs innecesarios
   - Asegúrate de que todas las dependencias estén actualizadas

2. **Generar una APK firmada o un App Bundle**:
   - En Android Studio, ve a Build > Generate Signed Bundle/APK
   - Selecciona "Android App Bundle" o "APK" según prefieras
   - Crea una nueva keystore o utiliza una existente
     - **IMPORTANTE**: Guarda la keystore en un lugar seguro, la necesitarás para futuras actualizaciones
   - Completa la información requerida y genera el archivo

3. **Crear una cuenta de desarrollador de Google Play**:
   - Visita [la Consola de Google Play](https://play.google.com/console/signup)
   - Paga la tarifa única de registro ($25 USD)
   - Completa tu información de perfil y acepta los acuerdos

4. **Preparar los recursos para la ficha de la Play Store**:
   - Capturas de pantalla de la aplicación (al menos 2)
   - Gráfico de función (1024 x 500 px)
   - Icono de alta resolución (512 x 512 px)
   - Descripción breve y completa
   - Política de privacidad (obligatoria)

5. **Crear una nueva aplicación en la Consola de Google Play**:
   - Haz clic en "Crear aplicación"
   - Selecciona un idioma predeterminado
   - Introduce el nombre de tu aplicación
   - Especifica si es gratuita o de pago
   - Haz clic en "Crear"

6. **Completar la ficha de Play Store**:
   - En "Ficha de Play Store", completa todos los campos requeridos
   - Sube capturas de pantalla, iconos y otros recursos gráficos
   - Introduce la descripción completa y breve de la app

7. **Configurar la app**:
   - Completa la sección "Configuración de la app"
   - Establece categorías, etiquetas de contenido, etc.

8. **Subir la APK o App Bundle**:
   - Ve a la sección "Versiones > Producción"
   - Haz clic en "Crear nueva versión"
   - Sube la APK firmada o el App Bundle que generaste
   - Completa las notas de la versión
   - Haz clic en "Guardar" y luego en "Revisar"

9. **Revisar y lanzar**:
   - Asegúrate de que todas las secciones estén completadas (verás marcas de verificación verdes)
   - Haz clic en "Iniciar revisión"
   - Google Play revisará tu aplicación, lo que puede tomar varias horas o días
   - Una vez aprobada, tu aplicación estará disponible en Google Play Store

## Notas importantes

- La primera publicación puede tardar más tiempo en ser revisada
- Asegúrate de que tu aplicación cumpla con todas las políticas de Google Play
- Conserva tu archivo de keystore: si lo pierdes, no podrás actualizar tu aplicación
- Considera implementar análisis para rastrear el uso y problemas de tu aplicación

## Solución de problemas comunes

1. **Problemas con la cámara**: Asegúrate de tener los permisos adecuados y de que tu dispositivo tenga una cámara compatible.
2. **Fallos al abrir URLs**: Verifica que la URL tenga el formato correcto y que el dispositivo tenga una aplicación capaz de manejar URLs.
3. **Problemas con el historial**: Verifica la implementación de Room y asegúrate de que la base de datos se esté inicializando correctamente.
4. **Problemas al escanear imágenes**: Si la aplicación no puede leer códigos QR desde imágenes guardadas, asegúrate de que los permisos de almacenamiento estén concedidos y que la imagen contenga un código QR claro y legible.
5. **Permisos de almacenamiento**: En Android 13 (API 33) y superior, se requiere el permiso READ_MEDIA_IMAGES en lugar de READ_EXTERNAL_STORAGE. La aplicación maneja esto automáticamente dependiendo de la versión de Android.
