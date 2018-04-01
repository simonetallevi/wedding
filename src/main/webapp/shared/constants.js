(function() {
    'use strict';

    angular.module('wedding')
        .constant('MAP', {
            lat: 39.201810,
            long: 9.563455,
            styles: [{
                    "featureType": "administrative",
                    "elementType": "geometry",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "administrative.land_parcel",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "administrative.neighborhood",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "poi",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "poi",
                    "elementType": "labels.text",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "poi.park",
                    "elementType": "labels.text.stroke",
                    "stylers": [{
                        "color": "#ffc7c1"
                    }]
                },
                {
                    "featureType": "road",
                    "elementType": "labels",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "road",
                    "elementType": "labels.icon",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "transit",
                    "stylers": [{
                        "visibility": "off"
                    }]
                },
                {
                    "featureType": "water",
                    "elementType": "geometry.fill",
                    "stylers": [{
                        "color": "#02ade4"
                    }]
                },
                {
                    "featureType": "water",
                    "elementType": "labels.text",
                    "stylers": [{
                        "visibility": "off"
                    }]
                }
            ]
        })
        .constant('HONEYMOON', {
            lat: -16.397615,
            long: 25.610464,
            itineraries:[{
                lat: -22.560680,
                long: 17.066360,
                title: "Windhoek, Namibia",
                link: "https://www.google.com/maps/place/Windhoek,+Namibia/@-22.5635825,16.9920139,12z/data=!3m1!4b1!4m5!3m4!1s0x1c0b1b5cb30c01ed:0xe4b84940cc445d3b!8m2!3d-22.5608807!4d17.0657549"
            }]
        });
})();