(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('DataPointDialogController', DataPointDialogController);

    DataPointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DataPoint'];

    function DataPointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DataPoint) {
        var vm = this;

        vm.dataPoint = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dataPoint.id !== null) {
                DataPoint.update(vm.dataPoint, onSaveSuccess, onSaveError);
            } else {
                DataPoint.save(vm.dataPoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:dataPointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
