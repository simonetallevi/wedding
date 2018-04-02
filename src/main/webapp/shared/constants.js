(function() {
    'use strict';

    angular.module('wedding')
        .constant('MAP', {
            lat: 39.201810,
            long: 9.563455,
            circle: {
              path: 'M-4,1a5,5 0 1,0 10,0a5,5 0 1,0 -10,0',
              fillColor: 'red',
              fillOpacity: 0.8,
              scale: 1,
              strokeWeight: 0
            },
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
            lat: -12.168527,
            long: 26.384052,
            itinerary:[
            {
                lat: -22.560680,
                long: 17.066360,
                title: "Windhoek, Namibia",
                link: "https://www.google.com/maps/place/Windhoek,+Namibia/@-22.5635825,16.9920139,12z/data=!3m1!4b1!4m5!3m4!1s0x1c0b1b5cb30c01ed:0xe4b84940cc445d3b!8m2!3d-22.5608807!4d17.0657549"
            },
            {
                lat: -24.728065,
                long: 15.341947,
                title: "Sossuvlei",
                link: "https://goo.gl/maps/uPu9HZ2GJC62"
            },
            {
                lat: -22.646732,
                long: 14.600494,
                title: "Swakopmund",
                link: "https://goo.gl/maps/ZmYJWyUTGLB2"
            },
            {
                lat: -18.855151,
                long: 16.330585,
                title: "Etosha National Park",
                link: "https://goo.gl/maps/jwZTVpfgWQT2"
            },
            {
                lat: -19.574027,
                long: 18.109467,
                title: "Grootfontein",
                link: "https://goo.gl/maps/HFhAFaYGAHJ2"
            },
            {
                lat: -18.119482,
                long: 21.582721,
                title: "Popa Falls",
                link: "https://goo.gl/maps/huavQvbfskz"
            },
            {
                lat: -17.792555,
                long: 25.154940,
                title: "Chobe River",
                link: "https://goo.gl/maps/u3vKYFefT7t"
            },
            {
                lat: -17.924197,
                long: 25.857208,
                title: "Victoria Falls",
                link: "https://goo.gl/maps/qg45UW7C61M2"
            },
            {
                lat: -3.424684,
                long: 37.064815,
                title: "Kilimanjaro Airport",
                link: "https://goo.gl/maps/V35gwdEy4212"
            },
            {
                lat: -3.387036,
                long: 36.682007,
                title: "Arusha",
                link: "https://goo.gl/maps/F6cuVzzqSJ72"
            },
            {
                lat: -2.630480,
                long: 35.956993,
                title: "Lake Natron",
                link: "https://goo.gl/maps/2tWNQgV4RXG2"
            },
            {
                lat: -2.354706,
                long: 34.817375,
                title: "Serengeti National Park",
                link: "https://goo.gl/maps/HZbThJgarwu"
            },
            {
                lat: -3.209233,
                long: 35.477363,
                title: "Ngorongoro Crater",
                link: "https://goo.gl/maps/y4KLTN2fis62"
            },
            {
                lat: -5.889755,
                long: 39.353804,
                title: "Zanzibar",
                link: "https://goo.gl/maps/W4X6ArACqB72"
            }]
        });
})();