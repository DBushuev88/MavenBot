import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String message, Model model) throws IOException {
        /*02fbbe56ce7bbbf2bb657a45bc6c65ab
         * Токен, генерируемый на openweathermap
         * после того как зарегистрируешся там.
         * Обратите внимание - после создания, необходимо ждать активации на сайте около суток.
         * Иначе будут ошибки - ругаться что токена не существует */
        //units=metric - погода в цельсиях
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=02fbbe56ce7bbbf2bb657a45bc6c65ab");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        //каст Json обьекта - получаем обьект
        JSONObject object = new JSONObject(result);
        //обращение к модели Model.java
        model.setName(object.getString("name"));
        /*все это находится в resources/json, сначала парсим большую модель, а здесь обращаемся уже к мелким помоделькам
        * я брал чтобы парсить из этого JSON обьекта - название города, влажность, температуру, иконку (картинку погоды), описание погоды (main) weather*/
        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            //во всех подобных местах требуется приведение типов
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity:" + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
