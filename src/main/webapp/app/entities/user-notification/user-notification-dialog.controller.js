(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserNotificationDialogController', UserNotificationDialogController);

    UserNotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserNotification', 'UserInfo', 'Alert', 'PathWay'];

    function UserNotificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserNotification, UserInfo, Alert, PathWay) {
        var vm = this;

        vm.userNotification = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userinfos = UserInfo.query();
        vm.alerts = Alert.query();
        vm.pathways = PathWay.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userNotification.id !== null) {
                UserNotification.update(vm.userNotification, onSaveSuccess, onSaveError);
            } else {
                UserNotification.save(vm.userNotification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:userNotificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
