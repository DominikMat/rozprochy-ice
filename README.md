# Zadanie I1 - Wywołanie dynamiczne
Celem zadania jest demonstracja działania wywołania dynamicznego
po stronie klienta middleware. Wywołanie dynamiczne to takie, w
którym nie jest wymagana znajomość interfejsu zdalnego obiektu
lub usługi w czasie kompilacji, lecz dopiero w czasie wykonania
(w zadaniu: klient ma nie mieć dołączonych żadnych klas/bibliotek
stub będących wynikiem kompilacji IDL). Wywołania mają być zrealizowane
dla kilku (co najmniej trzech) różnych operacji/procedur używających
przynajmniej w jednym przypadku nietrywialnych struktur danych (np. listy
(sekwencji), struktur) i sposobu komunikacji (gRPC: wywołanie strumieniowe).

Nie trzeba tworzyć żadnego formatu opisującego żądania użytkownika ani
parsera jego żądań - wystarczy zawrzeć to wywołanie "na sztywno" w kodzie
źródłowym, co najwyżej z konsoli parametryzując szczegóły danych. Jako
bazę można wykorzystać projekt z zajęć. Trzeba przemyśleć i umieć przedyskutować
 przydatność takiego podejścia w budowie aplikacji rozproszonych.

ICE: 
Dynamic Invocation [https://doc.zeroc.com/ice/3.7/client-server-features/dynamic-ice/dynamic-invocation-and-dispatch](https://doc.zeroc.com/ice/3.7/client-server-features/dynamic-ice/dynamic-invocation-and-dispatch) \
Odnośnie ograniczeń warto spojrzeć tu [https://doc.zeroc.com/ice/3.7/client-server-features/dynamic-ice/streaming-interfaces](https://doc.zeroc.com/ice/3.7/client-server-features/dynamic-ice/streaming-interfaces)

Warto spojrzeć na kod klas wygenerowanych dla struktur definiowanych struktur danych.
gRPC: Dopuszczalne (rekomendowane?) jest użycie usługi refleksji.
Równorzędnymi funkcjonalnie (w stosunku do stworzonego klienta) narzędziami są
grpcurl oraz Postman - należy umieć zademonstrować ich działanie w czasie oddawania zadania.

- Technologia middleware: **Ice** albo gRPC
- Języki programowania: dwa różne (jeden dla klienta, drugi dla serwera)
- Maksymalna punktacja: 8
