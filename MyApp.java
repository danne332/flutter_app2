
import 'dart:async';
import 'dart:convert';

import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

Future<Set<Marker>> getDogParkMarkers() async {
  final response =
      await http.get('https://github.com/danne332/test/raw/master/hund.json');

  if (response.statusCode == 200) {
    Set<Marker> tempMarkers = new Set<Marker>();
    List listOfParks = json.decode(response.body);
    for (int i = 0; i < listOfParks.length; i++) {
      tempMarkers.add(Marker(
          //icon:
            //  BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueGreen),
          markerId: MarkerId(i.toString()),
          infoWindow: InfoWindow(
            title: listOfParks[i]['name'],
            snippet: listOfParks[i]['desc'],
          ),
          position: LatLng(listOfParks[i]['lat'], listOfParks[i]['long'])));
    }
    return tempMarkers;
  } else {
    throw Exception('Failed to fetch hund.json');
  }
}

class _MyAppState extends State<MyApp> {
  Set<Marker> markers = Set();

  @override
  void initState() {
    super.initState();
    getData();
  }

  getData() async {
    markers = await getDogParkMarkers();
    setState(() {});
  }

  GoogleMapController mapController;

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Hundparker'),
          backgroundColor: Colors.green[700],
        ),
        body: GoogleMap(
          markers: Set.from(
            markers,
          ),
          onMapCreated: _onMapCreated,
          initialCameraPosition: CameraPosition(
            target: LatLng(59.2948966, 17.913921),
            zoom: 11.0,
          ),
        ),
      ),
    );
  }
}