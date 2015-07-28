angular.module('diffApp', ['ngRoute'])

    .config(function ($interpolateProvider, $locationProvider) {
        $interpolateProvider.startSymbol('[[').endSymbol(']]');
        $locationProvider.html5Mode(true).hashPrefix('!');
    })
    .factory('jQuery', ['$window', function ($window) {
        return $window.jQuery;
    }
    ])
    .value('springBootVersionURL', 'https://spring.io/project_metadata/spring-boot.json')

    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'FormController as form',
                templateUrl: 'diffListTemplate'
            })
            .when('/compare/:fromVersion/:toVersion', {
                controller: 'DiffController as diff',
                templateUrl: 'diffListTemplate'
            });
    })
    .service('ConfigDiff', ['$http', 'springBootVersionURL', function ($http, springBootVersionURL) {
        var fetchDiff = function (fromVersion, toVersion, full) {
            return $http.get("/diff/" + fromVersion + "/" + toVersion + "/")
                .success(function (data) {
                    /*
                     return {

                     fromVersion: data.leftVersion,
                     toVersion: data.rightVersion,
                     groups: data.allGroups
                     }
                     */
                    return data.allGroups;
                });
        };

        var fetchBootVersions = function () {
            return $http.get(springBootVersionURL);
        };

        return {
            fetchDiff: fetchDiff,
            fetchBootVersions: fetchBootVersions
        }
    }])
    .controller('FormController', ['$scope', '$location', 'jQuery', function ($scope, $location, $) {
        $scope.compare = function (fromVersion, toVersion) {
            $location.url("/compare/" + fromVersion + "/" + toVersion + "/");
        }
    }])
    .controller('DiffController', ['$scope', '$routeParams', 'ConfigDiff', '$location', '$anchorScroll', 'jQuery',
        function ($scope, $routeParams, ConfigDiff, $location, $anchorScroll, $) {

            $('body').scrollspy({target: '.navbar-side'});
            $('.navbar-side').affix({offset: {top: 10}});
            var fromVersion = $routeParams.fromVersion;
            var toVersion = $routeParams.toVersion;

            ConfigDiff.fetchDiff(fromVersion, toVersion, false)
                .success(function (data) {
                    $scope.diffs = data;
                });

            $scope.compare = function (fromVersion, toVersion) {
                $location.url("/compare/" + fromVersion + "/" + toVersion + "/");
            }
            $scope.fromVersion = fromVersion;
            $scope.toVersion = toVersion;
            $scope.gotoAnchor = function (e, group) {
                $anchorScroll(group.id.replace(/\./g, "-"));
                e.preventDefault();
            }
        }])

    .filter('anchor', function () {
        return function (input) {
            return input ? input.replace(/\./g, "-") : "";
        };
    })
    .filter('cssDiffClass', function () {
        return function (property) {
            switch (property.diffType) {
                case "ADD":
                    return "success";
                case "DELETE":
                    return "danger";
            }
        };
    });