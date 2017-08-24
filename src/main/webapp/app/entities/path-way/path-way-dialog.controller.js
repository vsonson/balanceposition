(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathWayDialogController', PathWayDialogController);

    PathWayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PathWay', 'UserNotification', 'PathStep'];

    function PathWayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PathWay, UserNotification, PathStep) {
        var vm = this;

        vm.pathWay = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usernotifications = UserNotification.query();
        vm.pathsteps = PathStep.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pathWay.id !== null) {
                PathWay.update(vm.pathWay, onSaveSuccess, onSaveError);
            } else {
                PathWay.save(vm.pathWay, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:pathWayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
