(function() {
    'use strict';

    angular
        .module('myCompanyApp')
        .controller('KaryawanDetailController', KaryawanDetailController);

    KaryawanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Karyawan', 'Company'];

    function KaryawanDetailController($scope, $rootScope, $stateParams, previousState, entity, Karyawan, Company) {
        var vm = this;

        vm.karyawan = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myCompanyApp:karyawanUpdate', function(event, result) {
            vm.karyawan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
