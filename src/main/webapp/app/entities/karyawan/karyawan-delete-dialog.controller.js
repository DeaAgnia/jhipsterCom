(function() {
    'use strict';

    angular
        .module('myCompanyApp')
        .controller('KaryawanDeleteController',KaryawanDeleteController);

    KaryawanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Karyawan'];

    function KaryawanDeleteController($uibModalInstance, entity, Karyawan) {
        var vm = this;

        vm.karyawan = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Karyawan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
