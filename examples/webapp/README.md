To run the demo webapp:

```
$ wget https://dl.fbaipublicfiles.com/fasttext/supervised-models/lid.176.ftz -O /tmp/lid.176.ftz
$ mvn clean spring-boot:run -Drun.arguments=--fastTextModel=/tmp/lid.176.ftz
```

Then call the service to detect the language of an input text as follows:
```
$ curl http://localhost:8080/langDetect?text=Hello%20World
```
