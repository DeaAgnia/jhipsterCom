(function() {
    'use strict';

    angular
        .module('myCompanyApp')
        .controller('KaryawanDialogController', KaryawanDialogController);

    KaryawanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Karyawan', 'Company'];

    function KaryawanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Karyawan, Company) {
        var vm = this;

        vm.karyawan = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.karyawan.id !== null) {
                Karyawan.update(vm.karyawan, onSaveSuccess, onSaveError);
            } else {
                Karyawan.save(vm.karyawan, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myCompanyApp:karyawanUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
