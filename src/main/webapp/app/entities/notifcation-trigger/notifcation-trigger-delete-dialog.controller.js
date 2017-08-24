(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NotifcationTriggerDeleteController',NotifcationTriggerDeleteController);

    NotifcationTriggerDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotifcationTrigger'];

    function NotifcationTriggerDeleteController($uibModalInstance, entity, NotifcationTrigger) {
        var vm = this;

        vm.notifcationTrigger = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NotifcationTrigger.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
