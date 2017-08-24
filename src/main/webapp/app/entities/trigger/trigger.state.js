(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trigger', {
            parent: 'entity',
            url: '/trigger?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Triggers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trigger/triggers.html',
                    controller: 'TriggerController',
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
        .state('trigger-detail', {
            parent: 'trigger',
            url: '/trigger/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Trigger'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trigger/trigger-detail.html',
                    controller: 'TriggerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Trigger', function($stateParams, Trigger) {
                    return Trigger.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trigger',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trigger-detail.edit', {
            parent: 'trigger-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trigger/trigger-dialog.html',
                    controller: 'TriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trigger', function(Trigger) {
                            return Trigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trigger.new', {
            parent: 'trigger',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trigger/trigger-dialog.html',
                    controller: 'TriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                triggerMetric: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trigger', null, { reload: 'trigger' });
                }, function() {
                    $state.go('trigger');
                });
            }]
        })
        .state('trigger.edit', {
            parent: 'trigger',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trigger/trigger-dialog.html',
                    controller: 'TriggerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trigger', function(Trigger) {
                            return Trigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trigger', null, { reload: 'trigger' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trigger.delete', {
            parent: 'trigger',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trigger/trigger-delete-dialog.html',
                    controller: 'TriggerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trigger', function(Trigger) {
                            return Trigger.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trigger', null, { reload: 'trigger' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
