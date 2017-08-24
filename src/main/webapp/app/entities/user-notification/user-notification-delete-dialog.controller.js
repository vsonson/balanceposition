(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserNotificationDeleteController',UserNotificationDeleteController);

    UserNotificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserNotification'];

    function UserNotificationDeleteController($uibModalInstance, entity, UserNotification) {
        var vm = this;

        vm.userNotification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserNotification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
