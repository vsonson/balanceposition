(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NetworkMemberDeleteController',NetworkMemberDeleteController);

    NetworkMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'NetworkMember'];

    function NetworkMemberDeleteController($uibModalInstance, entity, NetworkMember) {
        var vm = this;

        vm.networkMember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NetworkMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
