(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('AlertDeleteController',AlertDeleteController);

    AlertDeleteController.$inject = ['$uibModalInstance', 'entity', 'Alert'];

    function AlertDeleteController($uibModalInstance, entity, Alert) {
        var vm = this;

        vm.alert = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Alert.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
