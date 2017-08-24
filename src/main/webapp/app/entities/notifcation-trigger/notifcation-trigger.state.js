(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notifcation-trigger', {
            parent: 'entity',
            url: '/notifcation-trigger?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotifcationTriggers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-triggers.html',
                    controller: 'NotifcationTriggerController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('notifcation-trigger-detail', {
            parent: 'notifcation-trigger',
            url: '/notifcation-trigger/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NotifcationTrigger'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-trigger-detail.html',
                    controller: 'NotifcationTriggerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NotifcationTrigger', function($stateParams, NotifcationTrigger) {
                    return NotifcationTrigger.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notifcation-trigger',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notifcation-trigger-detail.edit', {
            parent: 'notifcation-trigger-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-trigger-dialog.html',
                    controller: 'NotifcationTriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotifcationTrigger', function(NotifcationTrigger) {
                            return NotifcationTrigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notifcation-trigger.new', {
            parent: 'notifcation-trigger',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-trigger-dialog.html',
                    controller: 'NotifcationTriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                desc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notifcation-trigger', null, { reload: 'notifcation-trigger' });
                }, function() {
                    $state.go('notifcation-trigger');
                });
            }]
        })
        .state('notifcation-trigger.edit', {
            parent: 'notifcation-trigger',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-trigger-dialog.html',
                    controller: 'NotifcationTriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotifcationTrigger', function(NotifcationTrigger) {
                            return NotifcationTrigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notifcation-trigger', null, { reload: 'notifcation-trigger' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notifcation-trigger.delete', {
            parent: 'notifcation-trigger',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notifcation-trigger/notifcation-trigger-delete-dialog.html',
                    controller: 'NotifcationTriggerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotifcationTrigger', function(NotifcationTrigger) {
                            return NotifcationTrigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notifcation-trigger', null, { reload: 'notifcation-trigger' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
