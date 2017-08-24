(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('AlertDialogController', AlertDialogController);

    AlertDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Alert', 'UserNotification'];

    function AlertDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Alert, UserNotification) {
        var vm = this;

        vm.alert = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usernotifications = UserNotification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.alert.id !== null) {
                Alert.update(vm.alert, onSaveSuccess, onSaveError);
            } else {
                Alert.save(vm.alert, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:alertUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
