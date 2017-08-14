(function() {
    'use strict';
    angular
        .module('myCompanyApp')
        .factory('Karyawan', Karyawan);

    Karyawan.$inject = ['$resource'];

    function Karyawan ($resource) {
        var resourceUrl =  'api/karyawans/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
